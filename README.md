# creddit
Clojure wrapper for Reddit API  


## Installation

Declare creddit in your project.clj:

`(defproject xxxx "1.0.0-SNAPSHOT"
  :dependencies [[creddit "0.1.0"]])`
  
Use creddit in your clojure code:

`(require '[creddit.core :as creddit])`  


## Usage

Initialise the creddit client with your Reddit application credentials found here https://www.reddit.com/prefs/apps/

I would recommend using a library like [cprop](https://github.com/tolitius/cprop) to load them in

Eg: {:credentials {:user-client <USER_CLIENT>, :user-secret <USER_SECRET>, :refresh-token <REFRESH_TOKEN>}}

`(def creddit-client (creddit/init (:credentials config)))`  


## Functions

**Frontpage** - *Retrieve posts from frontpage*

Usage: `(creddit/frontpage creddit-client limit)`

limit: Number of posts to retrieve (min: 1, max: 100, default: 10)  


## Copyright & License

Copyright (c) 2017 Conor Hughes - Released under the MIT license.

