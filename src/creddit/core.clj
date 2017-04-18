(ns creddit.core
  (:require [creddit.client :as client]))

(defprotocol RedditApi
  (frontpage [this] [this limit]))

(defrecord CredditClient [credentials]
  RedditApi
  "Retrieve posts from frontpage (Default: 10, Max: 100)"
  (frontpage [this]
    (client/frontpage credentials))
  (frontpage [this limit]
    (client/frontpage credentials limit)))

(defn init
  [credentials]
  (let [response (client/get-access-token credentials)]
    (-> credentials
        (assoc :access-token (:access_token response))
        (assoc :expires (+ (System/currentTimeMillis) (:expires_in response)))
        (CredditClient.))))