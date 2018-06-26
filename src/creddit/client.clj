(ns creddit.client
  (:require [clj-http.client :as client]
            [clojure.string :as string]
            [cheshire.core :refer :all]
            [slingshot.slingshot :refer :all])
  (:import (java.net URLEncoder)))

(defn- parse-response
  [response]
  (if-let [data (or (get-in response [:data :children])
                    (get-in response [:data :trophies]))]
    (reduce
      (fn [posts post]
        (conj posts (:data post)))
      []
      data)
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

(defn get-access-token
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

(defn- http-get [credentials url]
  (-> (client/get url
                  {:basic-auth [(:access-token credentials)]
                   :headers {"User-Agent" "creddit"}
                   :socket-timeout 10000
                   :conn-timeout 10000
                   :as :json})
      (get :body)))

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

(defn subreddit-search
  [credentials subreddit query limit]
  (if (valid-limit? limit)
    (-> (http-get credentials (str "https://www.reddit.com/r/" subreddit "/search/.json?q=" query "&limit=" limit))
        (parse-response))))

(defn subreddit-about
  [credentials subreddit]
  (-> (http-get credentials (str "https://www.reddit.com/r/" subreddit "/about/.json"))
      (parse-response)))

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
