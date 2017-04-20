# creddit
Clojure wrapper for Reddit API  

<br>

## Installation

Declare creddit in your project.clj:

`(defproject xxxx "1.0.0-SNAPSHOT"
  :dependencies [[creddit "0.1.0"]])`
  
Use creddit in your clojure code:

`(require '[creddit.core :as creddit])`  

<br>

## Usage

You will need your Reddit application credentials found here https://www.reddit.com/prefs/apps/

I would recommend using a library such as [cprop](https://github.com/tolitius/cprop) or [environ](https://github.com/weavejester/environ/) to load them in

Your credentials should like like this:  

`{:user-client <USER_CLIENT>, :user-secret <USER_SECRET>, :refresh-token <REFRESH_TOKEN>}}`

Once you have your credentials loaded in you can initialise the client:

`(def creddit-client (creddit/init credentials))`  

<br>

## Functions

**Frontpage** - *Retrieve posts from frontpage*

`(creddit/frontpage creddit-client)`  
`(creddit/frontpage creddit-client limit)`

limit: Number of posts to retrieve (min: 1, max: 100, default: 10)  

**Subreddit** - *Retrieve posts from subreddit*

`(creddit/subreddit creddit-client subreddit)`  
`(creddit/subreddit creddit-client subreddit limit)`

subreddit: Name of subreddit (eg: "programming", "funny", "pics" etc...)  
limit: Number of posts to retrieve (min: 1, max: 100, default: 10)  

**Subreddits** - *Retrieve list of subreddits*

`(creddit/frontpage creddit-client)`  
`(creddit/frontpage creddit-client limit)`

limit: Number of subreddits to retrieve (min: 1, max: 100, default: 10)  

<br>

## Development

**Testing**

Create a profiles.clj

`{:test {:env {:user-client <USER_CLIENT>, :user-secret <USER_SECRET>, :refresh-token <REFRESH_TOKEN>}}}`

Run `lein test`

<br>

## Copyright & License

Copyright (c) 2017 Conor Hughes - Released under the MIT license.

