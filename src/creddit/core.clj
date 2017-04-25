(ns creddit.core
  (:require [creddit.client :as client]))

(defprotocol RedditApi
  (frontpage [this limit time])
  (controversial [this limit time])
  (new [this limit time])
  (rising [this limit time])
  (top [this limit time])
  (subreddit [this subreddit limit time])
  (subreddit-controversial [this subreddit limit time])
  (subreddit-new [this subreddit limit time])
  (subreddit-rising [this subreddit limit time])
  (subreddit-top [this subreddit limit time])
  (subreddits [this limit])
  (user [this username])
  (user-posts [this username limit time]))

(defrecord CredditClient [credentials]
  RedditApi
  (frontpage [this limit time] (client/frontpage credentials limit time))
  (controversial [this limit time] (client/controversial credentials limit time))
  (new [this limit time] (client/new credentials limit time))
  (rising [this limit time] (client/rising credentials limit time))
  (top [this limit time] (client/top credentials limit time))
  (subreddit [this subreddit limit time] (client/subreddit credentials subreddit limit time))
  (subreddit-controversial [this subreddit limit time] (client/subreddit-controversial credentials subreddit limit time))
  (subreddit-new [this subreddit limit time] (client/subreddit-new credentials subreddit limit time))
  (subreddit-rising [this subreddit limit time] (client/subreddit-rising credentials subreddit limit time))
  (subreddit-top [this subreddit limit time] (client/subreddit-top credentials subreddit limit time))
  (subreddits [this limit] (client/subreddits credentials limit))
  (user [this username] (client/user credentials username))
  (user-posts [this username limit time] (client/user-posts credentials username limit time)))

(defn init
  [credentials]
  (let [response (client/get-access-token credentials)]
    (-> credentials
        (assoc :access-token (:access_token response))
        (assoc :expires-in (+ (System/currentTimeMillis) (:expires_in response)))
        (CredditClient.))))