(ns creddit.test.client
  (:require [clojure.test :refer :all]
            [clj-http.fake :refer :all]
            [creddit.client :as client]))

(def creddit-client {})

(def reddit-response "{\"data\":{\"children\":[{\"data\":{\"message\":\"test\"}}]}}")

(def parsed-reddit-response [{:message "test"}])

(deftest test-frontpage
  (testing "Retrieve frontpage posts"
    (is (= (with-fake-routes-in-isolation
             {"https://www.reddit.com/.json?limit=10&t=hour"
              (fn [request]
                {:status 200 :headers {} :body reddit-response})}
             (client/frontpage creddit-client 10 :hour))
           parsed-reddit-response))
    (is (thrown? Exception (client/frontpage creddit-client "10" :hour)))
    (is (thrown? Exception (client/frontpage creddit-client 10 "hour")))))

(deftest test-controversial
  (testing "Retrieve controversial posts"
    (is (= (with-fake-routes
             {"https://www.reddit.com/controversial/.json?limit=10&t=day"
              (fn [request]
                {:status 200 :headers {} :body reddit-response})}
             (client/controversial creddit-client 10 :day))
           parsed-reddit-response))
    (is (thrown? Exception (client/controversial creddit-client "10" :day)))
    (is (thrown? Exception (client/controversial creddit-client 10 "day")))))

(deftest test-new
  (testing "Retrieve new posts"
    (is (= (with-fake-routes
             {"https://www.reddit.com/new/.json?limit=10&t=week"
              (fn [request]
                {:status 200 :headers {} :body reddit-response})}
             (client/new creddit-client 10 :week))
           parsed-reddit-response))
    (is (thrown? Exception (client/new creddit-client "10" :week)))
    (is (thrown? Exception (client/new creddit-client 10 "week")))))

(deftest test-rising
  (testing "Retrieve rising posts"
    (is (= (with-fake-routes
             {"https://www.reddit.com/rising/.json?limit=10&t=month"
              (fn [request]
                {:status 200 :headers {} :body reddit-response})}
             (client/rising creddit-client 10 :month))
           parsed-reddit-response))
    (is (thrown? Exception (client/rising creddit-client "10" :month)))
    (is (thrown? Exception (client/rising creddit-client 10 "month")))))

(deftest test-top
  (testing "Retrieve top posts"
    (is (= (with-fake-routes
             {"https://www.reddit.com/top/.json?limit=10&t=year"
              (fn [request]
                {:status 200 :headers {} :body reddit-response})}
             (client/top creddit-client 10 :year))
           parsed-reddit-response))
    (is (thrown? Exception (client/top creddit-client "10" :year)))
    (is (thrown? Exception (client/top creddit-client 10 "year")))))

(deftest test-subreddit
  (testing "Retrieve subreddit posts"
    (is (= (with-fake-routes
             {"https://www.reddit.com/r/programming/.json?limit=10&t=all"
              (fn [request]
                {:status 200 :headers {} :body reddit-response})}
             (client/subreddit creddit-client "programming" 10 :all))
           parsed-reddit-response))
    (is (thrown? Exception (client/subreddit creddit-client "programming" "10" :all)))
    (is (thrown? Exception (client/subreddit creddit-client "programming" 10 "all")))))

(deftest test-subreddit-controversial
  (testing "Retrieve controversial subreddit posts"
    (is (= (with-fake-routes
             {"https://www.reddit.com/r/politics/controversial/.json?limit=10&t=hour"
              (fn [request]
                {:status 200 :headers {} :body reddit-response})}
             (client/subreddit-controversial creddit-client "politics" 10 :hour))
           parsed-reddit-response))
    (is (thrown? Exception (client/subreddit-controversial creddit-client "politics" "10" :hour)))
    (is (thrown? Exception (client/subreddit-controversial creddit-client "politics" 10 "hour")))))

(deftest test-subreddit-new
  (testing "Retrieve new subreddit posts"
    (is (= (with-fake-routes
             {"https://www.reddit.com/r/funny/new/.json?limit=10&t=day"
              (fn [request]
                {:status 200 :headers {} :body reddit-response})}
             (client/subreddit-new creddit-client "funny" 10 :day))
           parsed-reddit-response))
    (is (thrown? Exception (client/subreddit-new creddit-client "funny" "10" :day)))
    (is (thrown? Exception (client/subreddit-new creddit-client "funny" 10 "day")))))

(deftest test-subreddit-rising
  (testing "Retrieve rising subreddit posts"
    (is (= (with-fake-routes
             {"https://www.reddit.com/r/askreddit/rising/.json?limit=10&t=week"
              (fn [request]
                {:status 200 :headers {} :body reddit-response})}
             (client/subreddit-rising creddit-client "askreddit" 10 :week))
           parsed-reddit-response))
    (is (thrown? Exception (client/subreddit-rising creddit-client "askreddit" "10" :week)))
    (is (thrown? Exception (client/subreddit-rising creddit-client "askreddit" 10 "week")))))

(deftest test-subreddit-top
  (testing "Retrieve top subreddit posts"
    (is (= (with-fake-routes
             {"https://www.reddit.com/r/aww/top/.json?limit=10&t=month"
              (fn [request]
                {:status 200 :headers {} :body reddit-response})}
             (client/subreddit-top creddit-client "aww" 10 :month))
           parsed-reddit-response))
    (is (thrown? Exception (client/subreddit-top creddit-client "aww" "10" :month)))
    (is (thrown? Exception (client/subreddit-top creddit-client "aww" 10 "month")))))

(deftest test-subreddit-comments
  (testing "Retrieve latest subreddit comments"
    (is (= (with-fake-routes
             {"https://www.reddit.com/r/notinteresting/comments/.json?limit=10"
              (fn [request]
                {:status 200 :headers {} :body reddit-response})}
             (client/subreddit-comments creddit-client "notinteresting" 10))
           parsed-reddit-response))
      (is (thrown? Exception (client/subreddit-comments creddit-client "notinteresting" "10")))))

(deftest test-subreddit-comments-after
  (testing "Retrieve subreddit comments after given ID"
    (is (= (with-fake-routes
             {"https://www.reddit.com/r/notinteresting/comments/.json?limit=10&t=all&after=t1_h36hinb"
              (fn [request]
                {:status 200 :headers {} :body reddit-response})}
             (client/subreddit-comments-after creddit-client "notinteresting" "h36hinb" 10 :all))
           parsed-reddit-response))
    (is (thrown? Exception (client/user-comments-after creddit-client "notinteresting" "h36hinb" "10" :all)))
    (is (thrown? Exception (client/user-comments-after creddit-client "notinteresting" "h36hinb" 10 "all")))))

(deftest test-subreddit-comments-before
  (testing "Retrieve subreddit comments before given ID"
    (is (= (with-fake-routes
             {"https://www.reddit.com/r/notinteresting/comments/.json?limit=10&t=all&before=t1_h36hinb"
              (fn [request]
                {:status 200 :headers {} :body reddit-response})}
             (client/subreddit-comments-before creddit-client "notinteresting" "h36hinb" 10 :all))
           parsed-reddit-response))
    (is (thrown? Exception (client/subreddit-comments-before creddit-client "notinteresting" "h36hinb" "10" :all)))
    (is (thrown? Exception (client/subreddit-comments-before creddit-client "notinteresting" "h36hinb" 10 "all")))))

(deftest test-subreddit-search
  (testing "Search subreddit posts"
    (is (= (with-fake-routes
             {"https://www.reddit.com/r/DIY/search/.json?q=ikea&limit=10"
              (fn [request]
                {:status 200 :headers {} :body reddit-response})}
             (client/subreddit-search creddit-client "DIY" "ikea" 10))
           parsed-reddit-response))
    (is (thrown? Exception (client/subreddit-search creddit-client "DIY" "ikea" "10")))))

(deftest test-subreddit-about
  (testing "Retrieve about data for subreddit"
    (is (= (with-fake-routes
             {"https://www.reddit.com/r/clojure/about/.json"
              (fn [request]
                {:status 200 :headers {} :body reddit-response})}
             (client/subreddit-about creddit-client "clojure"))
           parsed-reddit-response))))

(deftest test-subreddit-moderators
  (testing "Retrieve moderators of a subreddit"
    (is (= (with-fake-routes
             {"https://www.reddit.com/r/clojure/about/moderators/.json"
              (fn [request]
                {:status 200 :headers {} :body "{\"data\":{\"children\":[{\"message\":\"test\"}]}}"})}
             (client/subreddit-moderators creddit-client "clojure"))
           parsed-reddit-response))))

(deftest test-subreddits
  (testing "Retrieve subreddits"
    (is (= (with-fake-routes
             {"https://www.reddit.com/subreddits/.json?limit=10"
              (fn [request]
                {:status 200 :headers {} :body reddit-response})}
             (client/subreddits creddit-client 10)))
        {"message" "This is a test"})
    (is (thrown? Exception (client/subreddits creddit-client "10")))))

(deftest test-subreddits-new
  (testing "Retrieve new subreddits"
    (is (= (with-fake-routes
             {"https://www.reddit.com/subreddits/new/.json?limit=10"
              (fn [request]
                {:status 200 :headers {} :body reddit-response})}
             (client/subreddits-new creddit-client 10)))
        {"message" "This is a test"})
    (is (thrown? Exception (client/subreddits-new creddit-client "10")))))

(deftest test-subreddits-popular
  (testing "Retrieve popular subreddits"
    (is (= (with-fake-routes
             {"https://www.reddit.com/subreddits/popular/.json?limit=10"
              (fn [request]
                {:status 200 :headers {} :body reddit-response})}
             (client/subreddits-popular creddit-client 10))
           parsed-reddit-response))
    (is (thrown? Exception (client/subreddits-popular creddit-client "10")))))

(deftest test-subreddits-gold
  (testing "Retrieve gold subreddits"
    (is (= (with-fake-routes
             {"https://www.reddit.com/subreddits/gold/.json?limit=10"
              (fn [request]
                {:status 200 :headers {} :body reddit-response})}
             (client/subreddits-gold creddit-client 10))
           parsed-reddit-response))
    (is (thrown? Exception (client/subreddits-gold creddit-client "10")))))

(deftest test-subreddits-default
  (testing "Retrieve default subreddits"
    (is (= (with-fake-routes
             {"https://www.reddit.com/subreddits/default/.json?limit=10"
              (fn [request]
                {:status 200 :headers {} :body reddit-response})}
             (client/subreddits-default creddit-client 10))
           parsed-reddit-response))
    (is (thrown? Exception (client/subreddits-default creddit-client "10")))))

(deftest test-subreddits-search
  (testing "Search subreddits"
    (is (= (with-fake-routes
             {"https://www.reddit.com/subreddits/search/.json?q=clojure&limit=10"
              (fn [request]
                {:status 200 :headers {} :body reddit-response})}
             (client/subreddits-search creddit-client "clojure" 10))
           parsed-reddit-response))
    (is (thrown? Exception (client/subreddits-search creddit-client "clojure" "10")))))

(deftest test-user
  (testing "Retrieve user profile"
    (is (= (with-fake-routes
             {"https://www.reddit.com/user/thisisbillgates/about/.json"
              (fn [request]
                {:status 200 :headers {} :body reddit-response})}
             (client/user creddit-client "thisisbillgates"))
           parsed-reddit-response))))

(deftest test-user-posts
  (testing "Retrieve user posts"
    (is (= (with-fake-routes
             {"https://www.reddit.com/user/shitty_watercolour/submitted/.json?limit=10&t=year"
              (fn [request]
                {:status 200 :headers {} :body reddit-response})}
             (client/user-posts creddit-client "shitty_watercolour" 10 :year))
           parsed-reddit-response))
    (is (thrown? Exception (client/user-posts creddit-client "shitty_watercolour" "10" :year)))
    (is (thrown? Exception (client/user-posts creddit-client "shitty_watercolour" 10 "year")))))

(deftest test-user-comments
  (testing "Retrieve user comments"
    (is (= (with-fake-routes
             {"https://www.reddit.com/user/Poem_for_your_sprog/comments/.json?limit=10&t=all"
              (fn [request]
                {:status 200 :headers {} :body reddit-response})}
             (client/user-comments creddit-client "Poem_for_your_sprog" 10 :all))
           parsed-reddit-response))
    (is (thrown? Exception (client/user-comments creddit-client "Poem_for_your_sprog" "10" :all)))
    (is (thrown? Exception (client/user-comments creddit-client "Poem_for_your_sprog" 10 "all")))))

(deftest test-user-comments-after
  (testing "Retrieve user comments after given ID"
    (is (= (with-fake-routes
             {"https://www.reddit.com/user/Poem_for_your_sprog/comments/.json?limit=10&t=all&after=t1_h36hinb"
              (fn [request]
                {:status 200 :headers {} :body reddit-response})}
             (client/user-comments-after creddit-client "Poem_for_your_sprog" "h36hinb" 10 :all))
           parsed-reddit-response))
    (is (thrown? Exception (client/user-comments-after creddit-client "Poem_for_your_sprog" "h36hinb" "10" :all)))
    (is (thrown? Exception (client/user-comments-after creddit-client "Poem_for_your_sprog" "h36hinb" 10 "all")))))

(deftest test-user-comments-before
  (testing "Retrieve user comments before given ID"
    (is (= (with-fake-routes
             {"https://www.reddit.com/user/Poem_for_your_sprog/comments/.json?limit=10&t=all&before=t1_h36hinb"
              (fn [request]
                {:status 200 :headers {} :body reddit-response})}
             (client/user-comments-before creddit-client "Poem_for_your_sprog" "h36hinb" 10 :all))
           parsed-reddit-response))
    (is (thrown? Exception (client/user-comments-before creddit-client "Poem_for_your_sprog" "h36hinb" "10" :all)))
    (is (thrown? Exception (client/user-comments-before creddit-client "Poem_for_your_sprog" "h36hinb" 10 "all")))))

(deftest test-user-posts-after
  (testing "Retrieve user posts after given ID"
    (is (= (with-fake-routes
             {"https://www.reddit.com/user/Poem_for_your_sprog/submitted/.json?limit=10&t=all&after=t3_h36hinb"
              (fn [request]
                {:status 200 :headers {} :body reddit-response})}
             (client/user-posts-after creddit-client "Poem_for_your_sprog" "h36hinb" 10 :all))
           parsed-reddit-response))
    (is (thrown? Exception (client/user-posts-after creddit-client "Poem_for_your_sprog" "h36hinb" "10" :all)))
    (is (thrown? Exception (client/user-posts-after creddit-client "Poem_for_your_sprog" "h36hinb" 10 "all")))))

(deftest test-user-posts-before
  (testing "Retrieve user posts before given ID"
    (is (= (with-fake-routes
             {"https://www.reddit.com/user/Poem_for_your_sprog/submitted/.json?limit=10&t=all&before=t3_h36hinb"
              (fn [request]
                {:status 200 :headers {} :body reddit-response})}
             (client/user-posts-before creddit-client "Poem_for_your_sprog" "h36hinb" 10 :all))
           parsed-reddit-response))
    (is (thrown? Exception (client/user-posts-before creddit-client "Poem_for_your_sprog" "h36hinb" "10" :all)))
    (is (thrown? Exception (client/user-posts-before creddit-client "Poem_for_your_sprog" "h36hinb" 10 "all")))))

(deftest test-user-trophies
  (testing "Retrieve user trophies"
    (is (= (with-fake-routes
             {"https://www.reddit.com/user/_varga/trophies/.json"
              (fn [request]
                {:status 200 :headers {} :body reddit-response})}
             (client/user-trophies creddit-client "_varga"))
           parsed-reddit-response))))

(deftest test-users
  (testing "Retrieve users"
    (is (= (with-fake-routes
             {"https://www.reddit.com/users/.json?limit=10"
              (fn [request]
                {:status 200 :headers {} :body reddit-response})}
             (client/users creddit-client 10))
           parsed-reddit-response))
    (is (thrown? Exception (client/users creddit-client "10")))))

(deftest test-users-new
  (testing "Retrieve new users"
    (is (= (with-fake-routes
             {"https://www.reddit.com/users/new/.json?limit=10"
              (fn [request]
                {:status 200 :headers {} :body reddit-response})}
             (client/users-new creddit-client 10))
           parsed-reddit-response))
    (is (thrown? Exception (client/users-new creddit-client "10")))))

(deftest test-users-popular
  (testing "Retrieve popular users"
    (is (= (with-fake-routes
             {"https://www.reddit.com/users/popular/.json?limit=10"
              (fn [request]
                {:status 200 :headers {} :body reddit-response})}
             (client/users-popular creddit-client 10))
           parsed-reddit-response))
    (is (thrown? Exception (client/users-popular creddit-client "10")))))

(deftest test-listing
  (testing "Retrieve named posts"
    (is (= (with-fake-routes-in-isolation
             {"https://www.reddit.com/by_id/t3_7ktt45,t3_7jj5nf/.json"
              (fn [request]
                {:status 200 :headers {} :body reddit-response})}
             (client/listing creddit-client ["t3_7ktt45" "t3_7jj5nf"]))
           parsed-reddit-response))
    (is (thrown? Exception (client/listing creddit-client "t3_7jj5nf")))
    (is (thrown? Exception (client/listing creddit-client :t3_7jj5nf)))))
