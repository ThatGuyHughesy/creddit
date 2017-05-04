(ns creddit.test.client
  (:require [clojure.test :refer :all]
            [environ.core :refer [env]]
            [creddit.core :refer [init]]
            [creddit.client :as client]))

(def creddit-client (init {:user-client (env :user-client)
                           :user-secret (env :user-secret)
                           :refresh-token (env :refresh-token)}))

(deftest test-client
  (testing "Intialise creddit client"
    (is (and (not (nil? (-> creddit-client :credentials :access-token)))
             (not (nil? (-> creddit-client :credentials :expires-in)))))))

(deftest test-frontpage
  (testing "Retrieve frontpage posts"
    (is (= 2 (-> (client/frontpage creddit-client 2 :hour)
                  (count))))
    (is (thrown? Exception (-> (client/frontpage creddit-client "2" :hour)
                               (count))))
    (is (thrown? Exception (-> (client/frontpage creddit-client 2 "hour")
                               (count))))))

(deftest test-controversial
  (testing "Retrieve controversial posts"
    (is (= 2 (-> (client/controversial creddit-client 2 :hour)
                  (count))))
    (is (thrown? Exception (-> (client/controversial creddit-client "2" :hour)
                               (count))))
    (is (thrown? Exception (-> (client/controversial creddit-client 2 "hour")
                               (count))))))

(deftest test-new
  (testing "Retrieve new posts"
    (is (= 2 (-> (client/new creddit-client 2 :hour)
                  (count))))
    (is (thrown? Exception (-> (client/new creddit-client "2" :hour)
                               (count))))
    (is (thrown? Exception (-> (client/new creddit-client 2 "hour")
                               (count))))))

(deftest test-rising
  (testing "Retrieve rising posts"
    (is (= 2 (-> (client/rising creddit-client 2 :hour)
                  (count))))
    (is (thrown? Exception (-> (client/rising creddit-client "2" :hour)
                               (count))))
    (is (thrown? Exception (-> (client/rising creddit-client 2 "hour")
                               (count))))))

(deftest test-top
  (testing "Retrieve top posts"
    (is (= 2 (-> (client/top creddit-client 2 :hour)
                  (count))))
    (is (thrown? Exception (-> (client/top creddit-client "2" :hour)
                               (count))))
    (is (thrown? Exception (-> (client/top creddit-client 2 "hour")
                               (count))))))

(deftest test-subreddit
  (testing "Retrieve subreddit posts"
    (is (= 2 (-> (client/subreddit creddit-client "programming" 2 :hour)
                  (count))))
    (is (thrown? Exception (-> (client/subreddit creddit-client "programming" "2" :hour)
                               (count))))
    (is (thrown? Exception (-> (client/subreddit creddit-client "programming" 2 "hour")
                               (count))))))

(deftest test-subreddit-controversial
  (testing "Retrieve subreddit posts"
    (is (= 2 (-> (client/subreddit-controversial creddit-client "politics" 2 :hour)
                  (count))))
    (is (thrown? Exception (-> (client/subreddit-controversial creddit-client "politics" "2" :hour)
                               (count))))
    (is (thrown? Exception (-> (client/subreddit-controversial creddit-client "politics" 2 "hour")
                               (count))))))

(deftest test-subreddit-new
  (testing "Retrieve subreddit posts"
    (is (= 2 (-> (client/subreddit-new creddit-client "funny" 2 :hour)
                  (count))))
    (is (thrown? Exception (-> (client/subreddit-new creddit-client "funny" "2" :hour)
                               (count))))
    (is (thrown? Exception (-> (client/subreddit-new creddit-client "funny" 2 "hour")
                               (count))))))

(deftest test-subreddit-rising
  (testing "Retrieve subreddit posts"
    (is (= 2 (-> (client/subreddit-rising creddit-client "askreddit" 2 :day)
                  (count))))
    (is (thrown? Exception (-> (client/subreddit-rising creddit-client "askreddit" "2" :hour)
                               (count))))
    (is (thrown? Exception (-> (client/subreddit-rising creddit-client "askreddit" 2 "hour")
                               (count))))))

(deftest test-subreddit-top
  (testing "Retrieve subreddit posts"
    (is (= 2 (-> (client/subreddit-top creddit-client "aww" 2 :hour)
                  (count))))
    (is (thrown? Exception (-> (client/subreddit-top creddit-client "aww" "2" :hour)
                               (count))))
    (is (thrown? Exception (-> (client/subreddit-top creddit-client "aww" 2 "hour")
                               (count))))))

(deftest test-subreddits
  (testing "Retrieve subreddits"
    (is (= 2 (-> (client/subreddits creddit-client 2)
                  (count))))
    (is (thrown? Exception (-> (client/subreddits creddit-client "2")
                               (count))))))

(deftest test-user
  (testing "Retrieve user profile"
    (is (= "91u3j" (-> (client/user creddit-client "thisisbillgates")
                       (:id))))))

(deftest test-user-posts
  (testing "Retrieve user posts"
    (is (= 2 (-> (client/user-posts creddit-client "shitty_watercolour" 2 :hour)
                  (count))))
    (is (thrown? Exception (-> (client/user-posts creddit-client "shitty_watercolour" "2" :hour)
                               (count))))
    (is (thrown? Exception (-> (client/user-posts creddit-client "shitty_watercolour" 2 "hour")
                               (count))))))
