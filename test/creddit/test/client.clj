(ns creddit.test.client
  (:require [clojure.test :refer :all]
            [environ.core :refer [env]]
            [creddit.core :refer [init]]
            [creddit.client :as client]))

(def creddit-client (init {:user-client (env :user-client)
                           :user-secret (env :user-secret)}))

(deftest test-client
  (testing "Intialise creddit client"
    (is (and (not (nil? (-> creddit-client :credentials :access-token)))
             (not (nil? (-> creddit-client :credentials :expires-in)))))))

(deftest test-frontpage
  (testing "Retrieve frontpage posts"
    (is (= 10 (-> (client/frontpage creddit-client 10 :day)
                  (count))))
    (is (thrown? Exception (-> (client/frontpage creddit-client "10" :day)
                               (count))))
    (is (thrown? Exception (-> (client/frontpage creddit-client 10 "day")
                               (count))))))

(deftest test-controversial
  (testing "Retrieve controversial posts"
    (is (= 10 (-> (client/controversial creddit-client 10 :day)
                  (count))))
    (is (thrown? Exception (-> (client/controversial creddit-client "10" :day)
                               (count))))
    (is (thrown? Exception (-> (client/controversial creddit-client 10 "day")
                               (count))))))

(deftest test-new
  (testing "Retrieve new posts"
    (is (= 10 (-> (client/new creddit-client 10 :day)
                  (count))))
    (is (thrown? Exception (-> (client/new creddit-client "10" :day)
                               (count))))
    (is (thrown? Exception (-> (client/new creddit-client 10 "day")
                               (count))))))

(deftest test-rising
  (testing "Retrieve rising posts"
    (is (= 10 (-> (client/rising creddit-client 10 :day)
                  (count))))
    (is (thrown? Exception (-> (client/rising creddit-client "10" :day)
                               (count))))
    (is (thrown? Exception (-> (client/rising creddit-client 10 "day")
                               (count))))))

(deftest test-top
  (testing "Retrieve top posts"
    (is (= 10 (-> (client/top creddit-client 10 :day)
                  (count))))
    (is (thrown? Exception (-> (client/top creddit-client "10" :day)
                               (count))))
    (is (thrown? Exception (-> (client/top creddit-client 10 "day")
                               (count))))))

(deftest test-subreddit
  (testing "Retrieve subreddit posts"
    (is (= 10 (-> (client/subreddit creddit-client "programming" 10 :day)
                  (count))))
    (is (thrown? Exception (-> (client/subreddit creddit-client "programming" "10" :day)
                               (count))))
    (is (thrown? Exception (-> (client/subreddit creddit-client "programming" 10 "day")
                               (count))))))

(deftest test-subreddit-controversial
  (testing "Retrieve subreddit posts"
    (is (= 10 (-> (client/subreddit-controversial creddit-client "politics" 10 :day)
                  (count))))
    (is (thrown? Exception (-> (client/subreddit-controversial creddit-client "politics" "10" :day)
                               (count))))
    (is (thrown? Exception (-> (client/subreddit-controversial creddit-client "politics" 10 "day")
                               (count))))))

(deftest test-subreddit-new
  (testing "Retrieve subreddit posts"
    (is (= 10 (-> (client/subreddit-new creddit-client "funny" 10 :day)
                  (count))))
    (is (thrown? Exception (-> (client/subreddit-new creddit-client "funny" "10" :day)
                               (count))))
    (is (thrown? Exception (-> (client/subreddit-new creddit-client "funny" 10 "day")
                               (count))))))

(deftest test-subreddit-rising
  (testing "Retrieve subreddit posts"
    (is (= 10 (-> (client/subreddit-rising creddit-client "askreddit" 10 :day)
                  (count))))
    (is (thrown? Exception (-> (client/subreddit-rising creddit-client "askreddit" "10" :day)
                               (count))))
    (is (thrown? Exception (-> (client/subreddit-rising creddit-client "askreddit" 10 "day")
                               (count))))))

(deftest test-subreddit-top
  (testing "Retrieve subreddit posts"
    (is (= 10 (-> (client/subreddit-top creddit-client "aww" 10 :day)
                  (count))))
    (is (thrown? Exception (-> (client/subreddit-top creddit-client "aww" "10" :day)
                               (count))))
    (is (thrown? Exception (-> (client/subreddit-top creddit-client "aww" 10 "day")
                               (count))))))

(deftest test-subreddit-search
  (testing "Search subreddit"
    (is (= 10 (-> (client/subreddit-search creddit-client "DIY" "ikea" 10)
                  (count))))
    (is (thrown? Exception (-> (client/subreddit-search creddit-client "DIY" "ikea" "10")
                               (count))))))

(deftest test-subreddits
  (testing "Retrieve subreddits"
    (is (= 10 (-> (client/subreddits creddit-client 10)
                  (count))))
    (is (thrown? Exception (-> (client/subreddits creddit-client "10")
                               (count))))))

(deftest test-subreddits-new
  (testing "Retrieve subreddits"
    (is (= 10 (-> (client/subreddits-new creddit-client 10)
                  (count))))
    (is (thrown? Exception (-> (client/subreddits-new creddit-client "10")
                               (count))))))

(deftest test-subreddits-popular
  (testing "Retrieve subreddits"
    (is (= 10 (-> (client/subreddits-popular creddit-client 10)
                  (count))))
    (is (thrown? Exception (-> (client/subreddits-popular creddit-client "10")
                               (count))))))

(deftest test-subreddits-gold
  (testing "Retrieve subreddits"
    (is (= 0 (-> (client/subreddits-gold creddit-client 10)
                 (count))))
    (is (thrown? Exception (-> (client/subreddits-gold creddit-client "10")
                               (count))))))

(deftest test-subreddits-default
  (testing "Retrieve subreddits"
    (is (= 10 (-> (client/subreddits-default creddit-client 10)
                  (count))))
    (is (thrown? Exception (-> (client/subreddits-default creddit-client "10")
                               (count))))))

(deftest test-subreddits-search
  (testing "Search subreddits"
    (is (= 10 (-> (client/subreddits-search creddit-client "clojure" 10)
                  (count))))
    (is (thrown? Exception (-> (client/subreddits-search creddit-client "clojure" "10")
                               (count))))))

(deftest test-user
  (testing "Retrieve user profile"
    (is (= "91u3j" (-> (client/user creddit-client "thisisbillgates")
                       (:id))))))

(deftest test-user-posts
  (testing "Retrieve user posts"
    (is (= 10 (-> (client/user-posts creddit-client "shitty_watercolour" 10 :day)
                  (count))))
    (is (thrown? Exception (-> (client/user-posts creddit-client "shitty_watercolour" "10" :day)
                               (count))))
    (is (thrown? Exception (-> (client/user-posts creddit-client "shitty_watercolour" 10 "day")
                               (count))))))

(deftest test-user-comments
  (testing "Retrieve user comments"
    (is (= 10 (-> (client/user-comments creddit-client "Poem_for_your_sprog" 10 :day)
                  (count))))
    (is (thrown? Exception (-> (client/user-comments creddit-client "Poem_for_your_sprog" "10" :day)
                               (count))))
    (is (thrown? Exception (-> (client/user-comments creddit-client "Poem_for_your_sprog" 10 "day")
                               (count))))))

(deftest test-user-trophies
  (testing "Retrieve user trophies"
    (is (< 10 (-> (client/user-trophies creddit-client "_vargas_")
                  (count))))
    (is (thrown? Exception (-> (client/user-trophies creddit-client "_varga")
                               (count))))))

(deftest test-users
  (testing "Retrieve users"
    (is (= 10 (-> (client/users creddit-client 10)
                  (count))))
    (is (thrown? Exception (-> (client/users creddit-client "10")
                               (count))))))

(deftest test-users-new
  (testing "Retrieve new users"
    (is (= 10 (-> (client/users-new creddit-client 10)
                  (count))))
    (is (thrown? Exception (-> (client/users-new creddit-client "10")
                               (count))))))

(deftest test-users
  (testing "Retrieve popular users"
    (is (= 10 (-> (client/users-popular creddit-client 10)
                  (count))))
    (is (thrown? Exception (-> (client/users-popular creddit-client "10")
                               (count))))))