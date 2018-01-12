(defproject creddit "1.1.0-SNAPSHOT"
  :description "Clojure wrapper for Reddit API"
  :url "https://github.com/ThatGuyHughesy/creddit"
  :license {:name "MIT License"
            :url "https://opensource.org/licenses/MIT"}
  :author "Conor Hughes <hello@conorhughes.me>"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [cheshire "5.7.1"]
                 [clj-http "3.4.1"]
                 [slingshot "0.12.2"]]
  :dev-dependencies [[lein-clojars "0.9.1"]]
  :profiles {:test
             {:dependencies [[clj-http-fake "1.0.3"]]}})