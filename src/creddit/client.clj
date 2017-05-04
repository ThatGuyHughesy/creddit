(ns creddit.client
  (:require [clj-http.client :as client]
            [clojure.string :as string]
            [cheshire.core :refer :all]
            [slingshot.slingshot :refer :all])
  (:import (java.net URLEncoder)))

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

(defn get-access-token
  [credentials]
  (try+
    (-> (client/post "https://www.reddit.com/api/v1/access_token"
                     {:basic-auth [(:user-client credentials) (:user-secret credentials)]
                      :headers {"User-Agent" "creddit"}
                      :form-params {:grant_type "refresh_token"
                                    :refresh_token (:refresh-token credentials)}
                      :content-type "application/x-www-form-urlencoded"
                      :socket-timeout 10000
                      :conn-timeout 10000
                      :as :json})
        (get :body))
    (catch [:status 401] {}
      (throw
        (ex-info "Unauthorised, please check your credentials are correct."
                 {:causes :unauthorised})))))

(defn- http-get [url credentials]
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
    (-> (http-get (str "https://www.reddit.com/.json?limit=" limit "&t=" time) credentials)
        (parse-response))))

(defn controversial
  [credentials limit time]
  (if (and (valid-limit? limit) (valid-time? time))
    (-> (http-get (str "https://www.reddit.com/controversial/.json?limit=" limit "&t=" time) credentials)
        (parse-response))))

(defn new
  [credentials limit time]
  (if (and (valid-limit? limit) (valid-time? time))
    (-> (http-get (str "https://www.reddit.com/new/.json?limit=" limit "&t=" time) credentials)
        (parse-response))))

(defn rising
  [credentials limit time]
  (if (and (valid-limit? limit) (valid-time? time))
    (-> (http-get (str "https://www.reddit.com/rising/.json?limit=" limit "&t=" time) credentials)
        (parse-response))))

(defn top
  [credentials limit time]
  (if (and (valid-limit? limit) (valid-time? time))
    (-> (http-get (str "https://www.reddit.com/top/.json?limit=" limit "&t=" time) credentials)
        (parse-response))))

(defn subreddit
  [credentials subreddit limit time]
  (if (and (valid-limit? limit) (valid-time? time))
    (-> (http-get (str "https://www.reddit.com/r/" subreddit "/.json?limit=" limit "&t=" time) credentials)
        (parse-response))))

(defn subreddit-controversial
  [credentials subreddit limit time]
  (if (and (valid-limit? limit) (valid-time? time))
    (-> (http-get (str "https://www.reddit.com/r/" subreddit "/controversial/.json?limit=" limit "&t=" time) credentials)
        (parse-response))))

(defn subreddit-new
  [credentials subreddit limit time]
  (if (and (valid-limit? limit) (valid-time? time))
    (-> (http-get (str "https://www.reddit.com/r/" subreddit "/new/.json?limit=" limit "&t=" time) credentials)
        (parse-response))))

(defn subreddit-rising
  [credentials subreddit limit time]
  (if (and (valid-limit? limit) (valid-time? time))
    (-> (http-get (str "https://www.reddit.com/r/" subreddit "/rising/.json?limit=" limit "&t=" time) credentials)
        (parse-response))))

(defn subreddit-top
  [credentials subreddit limit time]
  (if (and (valid-limit? limit) (valid-time? time))
    (-> (http-get (str "https://www.reddit.com/r/" subreddit "/top/.json?limit=" limit "&t=" time) credentials)
        (parse-response))))

(defn subreddits
  [credentials limit]
  (if (valid-limit? limit)
    (-> (http-get (str "https://www.reddit.com/subreddits/.json?limit=" limit) credentials)
        (parse-response))))

(defn user
  [credentials username]
  (-> (http-get (str "https://www.reddit.com/user/" username "/about/.json") credentials)
      (parse-response)))

(defn user-posts
  [credentials username limit time]
  (if (and (valid-limit? limit) (valid-time? time))
    (-> (http-get (str "https://www.reddit.com/user/" username "/.json?limit=" limit "&t=" time) credentials)
        (parse-response))))