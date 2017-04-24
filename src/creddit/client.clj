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
  [credentials limit]
  (http-get (str "https://www.reddit.com/.json?limit=" limit) credentials))

(defn frontpage
  ([credentials] (-> (get-frontpage credentials 10)
                     (parse-response)))
  ([credentials limit] (-> (get-frontpage credentials (valid-limit? limit))
                           (parse-response))))

(defn- get-subreddit
  [credentials subreddit limit]
  (http-get (str "https://www.reddit.com/r/" subreddit "/.json?limit=" limit) credentials))

(defn subreddit
  ([credentials subreddit] (-> (get-subreddit credentials subreddit 10)
                               (parse-response)))
  ([credentials subreddit limit] (-> (get-subreddit credentials subreddit (valid-limit? limit))
                                     (parse-response))))

(defn- get-subreddits
  [credentials limit]
  (http-get (str "https://www.reddit.com/subreddits/.json?limit=" limit) credentials))

(defn subreddits
  ([credentials] (-> (get-subreddits credentials 10)
                     (parse-response)))
  ([credentials limit] (-> (get-subreddits credentials (valid-limit? limit))
                           (parse-response))))

(defn- get-user
  [credentials username]
  (http-get (str "https://www.reddit.com/user/" username "/about/.json") credentials))

(defn user
  [credentials username] (-> (get-user credentials username)
                             (parse-response)))

(defn- get-user-posts
  [credentials username limit]
  (http-get (str "https://www.reddit.com/user/" username "/.json?limit=" limit) credentials))

(defn user-posts
  ([credentials username] (-> (get-user-posts credentials username 10)
                              (parse-response)))
  ([credentials username limit] (-> (get-user-posts credentials username (valid-limit? limit))
                                    (parse-response))))