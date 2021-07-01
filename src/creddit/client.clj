(ns creddit.client
  (:require [clj-http.client :as client]
            [clojure.string :as string]
            [cheshire.core :refer :all]
            [slingshot.slingshot :refer :all]))

(defn- parse-response
  [response]
  (if-let [coll (or (get-in response [:data :children])
                    (get-in response [:data :trophies]))]
    (map :data coll)
    (:data response)))

(defn- valid-limit? [limit]
  (if (and (integer? limit)
           (<= 1 limit)
           (>= 100 limit))
    limit
    (throw
      (ex-info "Invalid limit - Must be an integer between 1 & 100."
               {:causes :invalid-limit}))))

(defn- valid-time? [time]
  (if (and (keyword? time)
           (contains? #{:hour :day :week :month :year :all} time))
    time
    (throw
      (ex-info "Invalid time - Must be one of the following: :hour, :day, :week, :month, :year, :all."
               {:causes :invalid-time}))))

(defn- valid-top-level-kind? [topLevelKind]
  (if (and (keyword? topLevelKind) (contains? #{:user :subreddit} topLevelKind))
    topLevelKind
    (throw 
      (ex-info "Invalid top level kind - Must be one of the following: :user, :subreddit."))))
  
(defn- valid-direction? [direction]
  (if (and (keyword? direction) (contains? #{:before :after} direction))
    direction
    (throw 
      (ex-info "Invalid direction - Must be one of the following: :before, :after."))))

(defn- valid-entity? [entity]
  (if (and (keyword? entity) (contains? #{:comment :submission} entity))
    entity
    (throw 
      (ex-info "Invalid direction - Must be one of the following: :comment, :submission."))))

(defn- item-code [entityKind]
  (case entityKind
    :comment "t1_"
    :submission "t3_"))

(defn- before-or-after [direction entityId entityKind]
  (case direction
    :before (str "&before=" (item-code entityKind) entityId)
    :after (str "&after=" (item-code entityKind) entityId)))

(defn get-access-token-with-user
  [credentials]
  (try+
    (-> (client/post "https://www.reddit.com/api/v1/access_token"
                     {:basic-auth [(:user-client credentials) (:user-secret credentials)]
                      :headers {"User-Agent" "creddit"}
                      :form-params {:grant_type "password"
                                    :device_id (str (java.util.UUID/randomUUID))
                                    :username (:username credentials)
                                    :password (:password credentials)}
                      :content-type "application/x-www-form-urlencoded"
                      :socket-timeout 10000
                      :conn-timeout 10000
                      :as :json})
        (get :body))
    (catch [:status 401] {}
      (throw
        (ex-info "Unauthorised, please check your credentials are correct."
                 {:causes :unauthorised})))))

(defn get-access-token-without-user
  [credentials]
  (try+
    (-> (client/post "https://www.reddit.com/api/v1/access_token"
                     {:basic-auth [(:user-client credentials) (:user-secret credentials)]
                      :headers {"User-Agent" "creddit"}
                      :form-params {:grant_type "client_credentials"
                                    :device_id (str (java.util.UUID/randomUUID))}
                      :content-type "application/x-www-form-urlencoded"
                      :socket-timeout 10000
                      :conn-timeout 10000
                      :as :json})
        (get :body))
    (catch [:status 401] {}
      (throw
        (ex-info "Unauthorised, please check your credentials are correct."
                 {:causes :unauthorised})))))

(defn get-access-token
  [credentials]
  (if (:username credentials) (get-access-token-with-user credentials) (get-access-token-without-user credentials)))

(defn- http-get [credentials url]
  (-> (client/get url
                  {:basic-auth [(:access-token credentials)]
                   :headers {"User-Agent" "creddit"}
                   :socket-timeout 10000
                   :conn-timeout 10000
                   :as :json})
      (get :body)))

(defn- parse-top-level [topLevelKind]
  (case topLevelKind
    :user (name topLevelKind)
    :subreddit "r"))

(defn- parse-entity-kind [entityKind]
  (case entityKind
    :comment "/comments"
    :submission "/submitted"))

(defn- get-entities-window
  [credentials slug entityId limit time direction entityKind topLevelKind]
  (if (and (valid-limit? limit) (valid-time? time) (valid-entity? entityKind) (valid-top-level-kind? topLevelKind) (valid-direction? direction))
    (-> (http-get credentials (str "https://www.reddit.com/" (parse-top-level topLevelKind) "/" slug (parse-entity-kind entityKind) "/.json?limit=" limit "&t=" (name time) (before-or-after direction entityId entityKind)))
        (parse-response))))

(defn frontpage
  [credentials limit time]
  (if (and (valid-limit? limit) (valid-time? time))
    (-> (http-get credentials (str "https://www.reddit.com/.json?limit=" limit "&t=" (name time)))
        (parse-response))))

(defn controversial
  [credentials limit time]
  (if (and (valid-limit? limit) (valid-time? time))
    (-> (http-get credentials (str "https://www.reddit.com/controversial/.json?limit=" limit "&t=" (name time)))
        (parse-response))))

(defn new
  [credentials limit time]
  (if (and (valid-limit? limit) (valid-time? time))
    (-> (http-get credentials (str "https://www.reddit.com/new/.json?limit=" limit "&t=" (name time)))
        (parse-response))))

(defn rising
  [credentials limit time]
  (if (and (valid-limit? limit) (valid-time? time))
    (-> (http-get credentials (str "https://www.reddit.com/rising/.json?limit=" limit "&t=" (name time)))
        (parse-response))))

(defn top
  [credentials limit time]
  (if (and (valid-limit? limit) (valid-time? time))
    (-> (http-get credentials (str "https://www.reddit.com/top/.json?limit=" limit "&t=" (name time)))
        (parse-response))))

(defn subreddit
  [credentials subreddit limit time]
  (if (and (valid-limit? limit) (valid-time? time))
    (-> (http-get credentials (str "https://www.reddit.com/r/" subreddit "/.json?limit=" limit "&t=" (name time)))
        (parse-response))))

(defn subreddit-controversial
  [credentials subreddit limit time]
  (if (and (valid-limit? limit) (valid-time? time))
    (-> (http-get credentials (str "https://www.reddit.com/r/" subreddit "/controversial/.json?limit=" limit "&t=" (name time)))
        (parse-response))))

(defn subreddit-new
  [credentials subreddit limit time]
  (if (and (valid-limit? limit) (valid-time? time))
    (-> (http-get credentials (str "https://www.reddit.com/r/" subreddit "/new/.json?limit=" limit "&t=" (name time)))
        (parse-response))))

(defn subreddit-rising
  [credentials subreddit limit time]
  (if (and (valid-limit? limit) (valid-time? time))
    (-> (http-get credentials (str "https://www.reddit.com/r/" subreddit "/rising/.json?limit=" limit "&t=" (name time)))
        (parse-response))))

(defn subreddit-top
  [credentials subreddit limit time]
  (if (and (valid-limit? limit) (valid-time? time))
    (-> (http-get credentials (str "https://www.reddit.com/r/" subreddit "/top/.json?limit=" limit "&t=" (name time)))
        (parse-response))))

(defn subreddit-comments
  [credentials subreddit limit]
  (if (valid-limit? limit)
    (-> (http-get credentials (str "https://www.reddit.com/r/" subreddit "/comments/.json?limit=" limit))
        (parse-response))))
      
(defn subreddit-comments-before
  [credentials subreddit commentId limit time]
  (get-entities-window credentials subreddit commentId limit time :before :comment :subreddit))

(defn subreddit-comments-after
  [credentials subreddit commentId limit time]
  (get-entities-window credentials subreddit commentId limit time :after :comment :subreddit))

(defn subreddit-search
  [credentials subreddit query limit]
  (if (valid-limit? limit)
    (-> (http-get credentials (str "https://www.reddit.com/r/" subreddit "/search/.json?q=" query "&limit=" limit))
        (parse-response))))

(defn subreddit-about
  [credentials subreddit]
  (-> (http-get credentials (str "https://www.reddit.com/r/" subreddit "/about/.json"))
      (parse-response)))

(defn subreddit-moderators
  [credentials subreddit]
  (-> (http-get credentials (str "https://www.reddit.com/r/" subreddit "/about/moderators/.json"))
      :data
      :children))

(defn subreddits
  [credentials limit]
  (if (valid-limit? limit)
    (-> (http-get credentials (str "https://www.reddit.com/subreddits/.json?limit=" limit))
        (parse-response))))

(defn subreddits-new
  [credentials limit]
  (if (valid-limit? limit)
    (-> (http-get credentials (str "https://www.reddit.com/subreddits/new/.json?limit=" limit))
        (parse-response))))

(defn subreddits-popular
  [credentials limit]
  (if (valid-limit? limit)
    (-> (http-get credentials (str "https://www.reddit.com/subreddits/popular/.json?limit=" limit))
        (parse-response))))

(defn subreddits-gold
  [credentials limit]
  (if (valid-limit? limit)
    (-> (http-get credentials (str "https://www.reddit.com/subreddits/gold/.json?limit=" limit))
        (parse-response))))

(defn subreddits-default
  [credentials limit]
  (if (valid-limit? limit)
    (-> (http-get credentials (str "https://www.reddit.com/subreddits/default/.json?limit=" limit))
        (parse-response))))

(defn subreddits-search
  [credentials subreddit limit]
  (if (valid-limit? limit)
    (-> (http-get credentials (str "https://www.reddit.com/subreddits/search/.json?q=" subreddit "&limit=" limit))
        (parse-response))))

(defn user
  [credentials username]
  (-> (http-get credentials (str "https://www.reddit.com/user/" username "/about/.json"))
      (parse-response)))

(defn user-posts
  [credentials username limit time]
  (if (and (valid-limit? limit) (valid-time? time))
    (-> (http-get credentials (str "https://www.reddit.com/user/" username "/submitted/.json?limit=" limit "&t=" (name time)))
        (parse-response))))

(defn user-comments
  [credentials username limit time]
  (if (and (valid-limit? limit) (valid-time? time))
    (-> (http-get credentials (str "https://www.reddit.com/user/" username "/comments/.json?limit=" limit "&t=" (name time)))
        (parse-response))))

(defn user-posts-after
  [credentials username postId limit time]
  (get-entities-window credentials username postId limit time :after :submission :user))

(defn user-posts-before
  [credentials username postId limit time]
  (get-entities-window credentials username postId limit time :before :submission :user))


(defn user-comments-before
  [credentials username commentId limit time]
  (get-entities-window credentials username commentId limit time :before :comment :user))

(defn user-comments-after
  [credentials username commentId limit time]
  (get-entities-window credentials username commentId limit time :after :comment :user))


(defn user-trophies
  [credentials username]
  (-> (http-get credentials (str "https://www.reddit.com/user/" username "/trophies/.json"))
      (parse-response)))

(defn users
  [credentials limit]
  (if (valid-limit? limit)
    (-> (http-get credentials (str "https://www.reddit.com/users/.json?limit=" limit))
        (parse-response))))

(defn users-new
  [credentials limit]
  (if (valid-limit? limit)
    (-> (http-get credentials (str "https://www.reddit.com/users/new/.json?limit=" limit))
        (parse-response))))

(defn users-popular
  [credentials limit]
  (if (valid-limit? limit)
    (-> (http-get credentials (str "https://www.reddit.com/users/popular/.json?limit=" limit))
        (parse-response))))

(defn listing
  [credentials names]
  (-> (http-get credentials (str "https://www.reddit.com/by_id/" (string/join "," names) "/.json"))
      (parse-response)))

; other functions use credentials directly, unsure why mine needs to get them from the client with get-in...
(defn submit
  [credentials subreddit kind title content]
  (-> (client/post "https://oauth.reddit.com/api/submit"
                   {:headers {:User-Agent "creddit"
                              :Authorization (str "bearer " (get-in credentials [:credentials :access-token]))}
                    :form-params {:sr subreddit
                                  :kind kind
                                  :title title
                                  (cond
                                    (= kind "self") :text
                                    (= kind "link") :url) content}
                    :socket-timeout 10000
                    :conn-timeout 10000
                    :as :json})
      (get :body)))
