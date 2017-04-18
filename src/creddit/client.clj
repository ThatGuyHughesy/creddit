(ns creddit.client
  (:require [clj-http.client :as client]
            [clojure.data.json :as json]
            [clojure.string :as string])
  (:import [java.net URLEncoder]))

(defn post-data [data]
  (string/join
    "&"
    (map
      #(str (name (key %))
            "=" (URLEncoder/encode (str (val %)) "utf8")) data)))

(defn get-access-token
  [credentials]
  (:body
    (client/post "https://www.reddit.com/api/v1/access_token"
                 {:basic-auth [(:user-client credentials) (:user-secret credentials)]
                  :headers {"User-Agent" "creddit"}
                  :content-type "application/x-www-form-urlencoded"
                  :body (post-data {"grant_type" "refresh_token"
                                    "refresh_token" (:refresh-token credentials)})
                  :as :json
                  :socket-timeout 10000
                  :conn-timeout 10000})))

(defn parse-response
  [response]
  (reduce
    (fn [posts post]
      (conj posts (:data post)))
    []
    response))

(defn validate-limit [limit]
  (if (and (integer? limit)
           (<= 1 limit)
           (>= 100 limit))
    limit
    10))

(defn get-frontpage
  [credentials limit]
  (client/get (str "http://www.reddit.com/.json?limit=" limit)
              {:basic-auth [(:access-token credentials)]
               :headers {"User-Agent" "creddit"}
               :content-type "application/x-www-form-urlencoded"
               :as :json
               :socket-timeout 10000
               :conn-timeout 10000}))

(defn frontpage
  ([credentials] (-> (get-frontpage credentials 10)
                     (get-in [:body :data :children])
                     (parse-response)))
  ([credentials limit] (-> (get-frontpage credentials (validate-limit limit))
                           (get-in [:body :data :children])
                           (parse-response))))