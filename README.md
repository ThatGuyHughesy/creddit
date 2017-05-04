# creddit
Clojure wrapper for Reddit API  

[![Build Status](https://travis-ci.org/ThatGuyHughesy/creddit.svg?branch=master)](https://travis-ci.org/ThatGuyHughesy/creddit)

## Installation

Declare creddit in your project.clj:
  
```clojure
(defproject xxxx "1.0.0-SNAPSHOT"
  :dependencies [[creddit "0.1.0"]])
```
  
Use creddit in your clojure code:


```clojure
(require '[creddit.core :as creddit])
```


## Usage

You will need your Reddit application credentials found here https://www.reddit.com/prefs/apps/

I would recommend using a library such as [cprop](https://github.com/tolitius/cprop) or [environ](https://github.com/weavejester/environ/) to load them in

Your credentials should like like this:  

```edn
{:user-client <USER_CLIENT>, 
 :user-secret <USER_SECRET>, 
 :refresh-token <REFRESH_TOKEN>}
```

Once you have your credentials loaded in you can initialise the client:

```clojure
(def creddit-client (creddit/init credentials))
```

## Functions

### Frontpage

*Retrieve posts from frontpage*

**limit:** Maximum number of posts to retrieve (Minimum: 1, Maximum: 100)  
**time:** Time span of query (One of :hour, :day, :week, :month, :year, :all)

```clojure
(creddit/frontpage creddit-client limit time)

(creddit/controversial creddit-client limit time)

(creddit/new creddit-client limit time)

(creddit/rising creddit-client limit time)

(creddit/top creddit-client limit time)
```

### Subreddit

*Retrieve posts from subreddit*

**subreddit:** Name of subreddit (Eg: "programming", "funny", "pics" etc...)  
**limit:** Maximum number of posts to retrieve (Minimum: 1, Maximum: 100)  
**time:** Time span of query (One of :hour, :day, :week, :month, :year, :all)

```clojure
(creddit/subreddit creddit-client subreddit limit time)

(creddit/subreddit-controversial creddit-client subreddit limit time)

(creddit/subreddit-new creddit-client subreddit limit time)

(creddit/subreddit-rising creddit-client subreddit limit time)

(creddit/subreddit-top creddit-client subreddit limit time)
```

### Subreddits

*Retrieve list of subreddits*

**limit:** Maximum number of subreddits to retrieve (Minimum: 1, Maximum: 100)   

```clojure
(creddit/subreddits creddit-client limit)
```


### User

*Retrieve user profile*

**username:** Name of user (Eg: "thisisbillgates", "awildsketchappeared", "way_fairer" etc...)  

```clojure
(creddit/user creddit-client username)
```

*Retrieve user posts*

**username:** Name of user (Eg: "thisisbillgates", "awildsketchappeared", "way_fairer" etc...)  
**limit:** Maximum number of posts to retrieve (Minimum: 1, Maximum: 100)  
**time:** Time span of query (One of :hour, :day, :week, :month, :year, :all)

```clojure
(creddit/user-posts creddit-client username limit time)
```

## Development

### Testing

Create a profiles.clj

```clojure
{:test 
  {:env 
    {:user-client <USER_CLIENT>, 
     :user-secret <USER_SECRET>, 
     :refresh-token <REFRESH_TOKEN>}}}
```

Run tests

```sh
$ lein test
```

## Copyright & License

Copyright (c) 2017 Conor Hughes - Released under the MIT license.

