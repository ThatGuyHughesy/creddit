# creddit
Clojure wrapper for Reddit API  

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

**limit:** Number of posts to retrieve (min: 1, max: 100, default: 10) 

```clojure
(creddit/frontpage creddit-client)
(creddit/frontpage creddit-client limit)
``` 

### Subreddit

*Retrieve posts from subreddit*

**subreddit:** Name of subreddit (eg: "programming", "funny", "pics" etc...)  
**limit:** Number of posts to retrieve (min: 1, max: 100, default: 10)  

```clojure
(creddit/subreddit creddit-client subreddit)

(creddit/subreddit creddit-client subreddit limit)
```

### Subreddits

*Retrieve list of subreddits*

**limit:** Number of subreddits to retrieve (min: 1, max: 100, default: 10)  

```clojure
(creddit/frontpage creddit-client)

(creddit/frontpage creddit-client limit)
```


### User

*Retrieve user profile*

**username:** Name of user (eg: "thisisbillgates", "awildsketchappeared", "way_fairer" etc...)  

```clojure
(creddit/user creddit-client username)
```

*Retrieve user posts*

**username:** Name of user (eg: "thisisbillgates", "awildsketchappeared", "way_fairer" etc...)  
**limit:** Number of posts to retrieve (min: 1, max: 100, default: 10)  

```clojure
(creddit/user-posts creddit-client username)

(creddit/user-posts creddit-client username limit)
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

