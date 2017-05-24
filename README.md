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
 :user-secret <USER_SECRET>}
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

*Search subreddit's posts*

**subreddit:** Name of subreddit (Eg: "programming", "funny", "pics" etc...)  
**query:** Search term (Eg: "clojure tutorials", "dank memes" etc...)  
**limit:** Maximum number of posts to retrieve (Minimum: 1, Maximum: 100)   

```clojure
(creddit/subreddit-search creddit-client subreddit query limit)
```

### Subreddits

*Retrieve list of subreddits*

**limit:** Maximum number of subreddits to retrieve (Minimum: 1, Maximum: 100)   

```clojure
(creddit/subreddits creddit-client limit)

(creddit/subreddits-new creddit-client limit)

(creddit/subreddits-popular creddit-client limit)

(creddit/subreddits-gold creddit-client limit)

(creddit/subreddits-default creddit-client limit)
```

*Search for subreddit*

**subreddit:** Subreddit search term (Eg: "programming", "clojure", "nodejs" etc...)  
**limit:** Maximum number of subreddits to retrieve (Minimum: 1, Maximum: 100)   

```clojure
(creddit/subreddits-search creddit-client subreddit limit)
```

### User

*Retrieve user profile*

**username:** Name of user (Eg: "thisisbillgates", "awildsketchappeared", "way_fairer" etc...)  

```clojure
(creddit/user creddit-client username)

(creddit/user-trophies creddit-client username)
```

*Retrieve user posts*

**username:** Name of user (Eg: "thisisbillgates", "awildsketchappeared", "way_fairer" etc...)  
**limit:** Maximum number of posts to retrieve (Minimum: 1, Maximum: 100)  
**time:** Time span of query (One of :hour, :day, :week, :month, :year, :all)

```clojure
(creddit/user-posts creddit-client username limit time)
```

*Retrieve user comments*

**username:** Name of user (Eg: "thisisbillgates", "awildsketchappeared", "way_fairer" etc...)  
**limit:** Maximum number of posts to retrieve (Minimum: 1, Maximum: 100)  
**time:** Time span of query (One of :hour, :day, :week, :month, :year, :all)

```clojure
(creddit/user-comments creddit-client username limit time)
```

### Users

*Retrieve users*

**limit:** Maximum number of users to retrieve (Minimum: 1, Maximum: 100)  

```clojure
(creddit/users creddit-client limit)

(creddit/users-new creddit-client limit)

(creddit/users-popular creddit-client limit)
```


## Development

### Testing

Create a profiles.clj

```clojure
{:test 
  {:env 
    {:user-client <USER_CLIENT>, 
     :user-secret <USER_SECRET>}}}
```

Run tests

```sh
$ lein test
```

## Copyright & License

Copyright (c) 2017 Conor Hughes - Released under the MIT license.

