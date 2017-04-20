(ns creddit.test.client
  (:require [clojure.test :refer :all]
            [environ.core :refer [env]]
            [creddit.core :refer [init]]
            [creddit.client :refer :all]))

(def creddit-client (init {:user-client (env :user-client)
                           :user-secret (env :user-secret)
                           :refresh-token (env :refresh-token)}))

(deftest test-client
  (testing "Intialise creddit client"
    (is (and (not (nil? (-> creddit-client :credentials :access-token)))
             (not (nil? (-> creddit-client :credentials :expires-in)))))))

(deftest test-frontpage
  (testing "Retrieve frontpage posts"
    (is (= 10 (count (frontpage (:credentials creddit-client)))))))

(deftest test-frontpage-limit
  (testing "Retrieve 50 frontpage posts"
    (is (= 50 (count (frontpage (:credentials creddit-client) 50))))))

(deftest test-subreddit
  (testing "Retrieve subreddit posts"
    (is (= 10 (count (subreddit (:credentials creddit-client) "programming"))))))

(deftest test-subreddit-limit
  (testing "Retrieve 50 subreddit posts"
    (is (= 50 (count (subreddit (:credentials creddit-client) "programming" 50))))))

(deftest test-subreddits
  (testing "Retrieve subreddits"
    (is (= 10 (count (subreddits (:credentials creddit-client)))))))

(deftest test-subreddits-limit
  (testing "Retrieve 50 subreddits"
    (is (= 50 (count (subreddits (:credentials creddit-client) 50))))))
