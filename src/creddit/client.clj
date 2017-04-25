(ns creddit.client
  (:require [clj-http.client :as client]
            [clojure.data.json :as json]
            [clojure.string :as string])
  (:import [java.net URLEncoder]))

(defn- post-data [data]
  (string/join
    "&"
    (map
      #(str (name (key %))
            "=" (URLEncoder/encode (str (val %)) "utf8")) data)))

(defn- parse-response
  [response]
  (if (get-in response [:data :children])
    (->> (get-in response [:data :children])
         (reduce
           (fn [posts post]
             (conj posts (:data post)))
           []))
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

(defn- http-get [url credentials]
  (-> (client/get url
                  {:basic-auth [(:access-token credentials)]
                   :headers {"User-Agent" "creddit"}
                   :content-type "application/x-www-form-urlencoded"
                   :as :json
                   :socket-timeout 10000
                   :conn-timeout 10000})
      (get :body)
      (json/read-str :key-fn keyword)))

(defn get-access-token
  [credentials]
  (-> (client/post "https://www.reddit.com/api/v1/access_token"
                   {:basic-auth [(:user-client credentials) (:user-secret credentials)]
                    :headers {"User-Agent" "creddit"}
                    :content-type "application/x-www-form-urlencoded"
                    :body (post-data {"grant_type" "refresh_token"
                                      "refresh_token" (:refresh-token credentials)})
                    :as :json
                    :socket-timeout 10000
                    :conn-timeout 10000})
      (get :body)
      (json/read-str :key-fn keyword)))

(defn- get-frontpage
  [credentials limit time]
  (http-get (str "https://www.reddit.com/.json?limit=" limit "&t=" time) credentials))

(defn frontpage
  [credentials limit time]
  (-> (get-frontpage credentials (valid-limit? limit) (valid-time? time))
      (parse-response)))

(defn- get-controversial
  [credentials limit time]
  (http-get (str "https://www.reddit.com/controversial/.json?limit=" limit "&t=" time) credentials))

(defn controversial
  [credentials limit time]
  (-> (get-controversial credentials (valid-limit? limit) (valid-time? time))
      (parse-response)))

(defn- get-new
  [credentials limit time]
  (http-get (str "https://www.reddit.com/new/.json?limit=" limit "&t=" time) credentials))

(defn new
  [credentials limit time]
  (-> (get-new credentials (valid-limit? limit) (valid-time? time))
      (parse-response)))

(defn- get-rising
  [credentials limit time]
  (http-get (str "https://www.reddit.com/rising/.json?limit=" limit "&t=" time) credentials))

(defn rising
  [credentials limit time]
  (-> (get-rising credentials (valid-limit? limit) (valid-time? time))
      (parse-response)))

(defn- get-top
  [credentials limit time]
  (http-get (str "https://www.reddit.com/top/.json?limit=" limit "&t=" time) credentials))

(defn top
  [credentials limit time]
  (-> (get-top credentials (valid-limit? limit) (valid-time? time))
      (parse-response)))

(defn- get-subreddit
  [credentials subreddit limit time]
  (http-get (str "https://www.reddit.com/r/" subreddit "/.json?limit=" limit "&t=" time) credentials))

(defn subreddit
  [credentials subreddit limit time]
  (-> (get-subreddit credentials subreddit (valid-limit? limit) (valid-time? time))
      (parse-response)))

(defn- get-subreddit-controversial
  [credentials subreddit limit time]
  (http-get (str "https://www.reddit.com/r/" subreddit "/controversial/.json?limit=" limit "&t=" time) credentials))

(defn subreddit-controversial
  [credentials subreddit limit time]
  (-> (get-subreddit-controversial credentials subreddit (valid-limit? limit) (valid-time? time))
      (parse-response)))

(defn- get-subreddit-new
  [credentials subreddit limit time]
  (http-get (str "https://www.reddit.com/r/" subreddit "/new/.json?limit=" limit "&t=" time) credentials))

(defn subreddit-new
  [credentials subreddit limit time]
  (-> (get-subreddit-new credentials subreddit (valid-limit? limit) (valid-time? time))
      (parse-response)))

(defn- get-subreddit-rising
  [credentials subreddit limit time]
  (http-get (str "https://www.reddit.com/r/" subreddit "/rising/.json?limit=" limit "&t=" time) credentials))

(defn subreddit-rising
  [credentials subreddit limit time]
  (-> (get-subreddit-rising credentials subreddit (valid-limit? limit) (valid-time? time))
      (parse-response)))

(defn- get-subreddit-top
  [credentials subreddit limit time]
  (http-get (str "https://www.reddit.com/r/" subreddit "/top/.json?limit=" limit "&t=" time) credentials))

(defn subreddit-top
  [credentials subreddit limit time]
  (-> (get-subreddit-top credentials subreddit (valid-limit? limit) (valid-time? time))
      (parse-response)))

(defn- get-subreddits
  [credentials limit]
  (http-get (str "https://www.reddit.com/subreddits/.json?limit=" limit) credentials))

(defn subreddits
  [credentials limit]
  (-> (get-subreddits credentials (valid-limit? limit))
      (parse-response)))

(defn- get-user
  [credentials username]
  (http-get (str "https://www.reddit.com/user/" username "/about/.json") credentials))

(defn user
  [credentials username]
  (-> (get-user credentials username)
      (parse-response)))

(defn- get-user-posts
  [credentials username limit time]
  (http-get (str "https://www.reddit.com/user/" username "/.json?limit=" limit "&t=" time) credentials))

(defn user-posts
  [credentials username limit time]
  (-> (get-user-posts credentials username (valid-limit? limit) (valid-time? time))
      (parse-response)))