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
    (is (= 10 (-> (frontpage creddit-client)
                  (count))))
    (is (= 50 (-> (frontpage creddit-client 50)
                  (count))))
    (is (thrown? Exception (-> (frontpage creddit-client "50")
                               (count))))
    (is (thrown? Exception (-> (frontpage creddit-client "abc")
                               (count))))))

(deftest test-subreddit
  (testing "Retrieve subreddit posts"
    (is (= 10 (-> (subreddit creddit-client "programming")
                  (count))))
    (is (= 50 (-> (subreddit creddit-client "programming" 50)
                  (count))))
    (is (thrown? Exception (-> (test-subreddit creddit-client "programming" "50")
                               (count))))
    (is (thrown? Exception (-> (test-subreddit creddit-client "programming" "abc")
                               (count))))))

(deftest test-subreddits
  (testing "Retrieve subreddits"
    (is (= 10 (-> (subreddits creddit-client)
                  (count))))
    (is (= 50 (-> (subreddits creddit-client 50)
                  (count))))
    (is (thrown? Exception (-> (subreddits creddit-client "50")
                               (count))))
    (is (thrown? Exception (-> (subreddits creddit-client "abc")
                               (count))))))

(deftest test-user
  (testing "Retrieve user profile"
    (is (= "91u3j" (-> (user creddit-client "thisisbillgates")
                       (:id))))))

(deftest test-user-posts
  (testing "Retrieve user posts"
    (is (= 10 (-> (user-posts creddit-client "thisisbillgates")
                  (count))))
    (is (= 50 (-> (user-posts creddit-client "thisisbillgates" 50)
                  (count))))
    (is (thrown? Exception (-> (user-posts creddit-client "thisisbillgates" "50")
                               (count))))
    (is (thrown? Exception (-> (user-posts creddit-client "thisisbillgates" "abc")
                               (count))))))
