# Creddit
Clojure wrapper for Reddit API  

[![Build Status](https://travis-ci.org/ThatGuyHughesy/creddit.svg?branch=master)](https://travis-ci.org/ThatGuyHughesy/creddit)
[![Clojars Project](https://img.shields.io/clojars/v/creddit.svg)](https://clojars.org/creddit)

## Installation

Declare creddit in your project.clj:

[![Clojars Project](http://clojars.org/creddit/latest-version.svg)](http://clojars.org/creddit)

Use creddit in your clojure code:

```clojure
(require '[creddit.core :as creddit])
```


## Usage

You will need your Reddit application credentials found here https://www.reddit.com/prefs/apps/

I would recommend using a library such as [cprop](https://github.com/tolitius/cprop) or [environ](https://github.com/weavejester/environ/) to load them in

Your credentials should like this:  

```edn
{:user-client <USER_CLIENT>,
 :user-secret <USER_SECRET>,
 :username <REDDIT_USERNAME>,
 :password <REDDIT_PASSWORD>}
```

The username and password are needed for some actions like submitting but are optional for anonymous actions.

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

*Retrieve recent comments from subreddit*

**subreddit:** Name of subreddit (Eg: "programming", "funny", "pics" etc...)  
**limit:** Maximum number of comments to retrieve (Minimum: 1, Maximum: 100)  
**commentId:** ID without [fullname](https://www.reddit.com/dev/api/#fullnames) prefix of the comment you would like to search from.

```clojure
(creddit/subreddit-comments creddit-client subreddit limit)

(creddit/subreddit-comments-after creddit-client subreddit commentId limit)

(creddit/subreddit-comments-before creddit-client subreddit commentId limit)
```

*Search subreddit's posts*

**subreddit:** Name of subreddit (Eg: "programming", "funny", "pics" etc...)  
**query:** Search term (Eg: "clojure tutorials", "dank memes" etc...)  
**limit:** Maximum number of posts to retrieve (Minimum: 1, Maximum: 100)   

```clojure
(creddit/subreddit-search creddit-client subreddit query limit)
```

*Retrieve about from subreddit*

**subreddit:** Name of subreddit (Eg: "programming", "funny", "pics" etc...)  

```clojure
(creddit/subreddit-about creddit-client subreddit)
```

*Retrieve moderators of a subreddit*

**subreddit:** Name of subreddit (Eg: "programming", "funny", "pics" etc...)

```clojure
(creddit/subreddit-moderators creddit-client subreddit)
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
**postId:** ID without [fullname](https://www.reddit.com/dev/api/#fullnames) prefix of the post you would like to search from.

```clojure
(creddit/user-posts creddit-client username limit time)

(creddit/user-posts-after creddit-client username postId limit time)

(creddit/user-posts-before creddit-client username postId limit time)
```

*Retrieve user comments*

**username:** Name of user (Eg: "thisisbillgates", "awildsketchappeared", "way_fairer" etc...)  
**limit:** Maximum number of posts to retrieve (Minimum: 1, Maximum: 100)  
**time:** Time span of query (One of :hour, :day, :week, :month, :year, :all)  
**commentId:** ID without [fullname](https://www.reddit.com/dev/api/#fullnames) prefix of the comment you would like to search from.

```clojure
(creddit/user-comments creddit-client username limit time)

(creddit/user-comments-after creddit-client username commentId limit time)

(creddit/user-comments-before creddit-client username commentId limit time)
```

### Users

*Retrieve users*

**limit:** Maximum number of users to retrieve (Minimum: 1, Maximum: 100)  

```clojure
(creddit/users creddit-client limit)

(creddit/users-new creddit-client limit)

(creddit/users-popular creddit-client limit)
```

### Listings

*Retrieve specific posts*

**names:** Sequence of fully specified [fullnames](https://www.reddit.com/dev/api#fullnames).

```clojure
(creddit/listing creddit-client names)
```

### Submitting

*Submit self or link posts*

**subreddit:** Name of subreddit (Eg: "programming", "funny", "pics" etc...)  
**kind:** "self" or "link" (image uploads are not supported)  
**title:** Title for the post (Eg: "test post please ignore")  
**content:** Text for self posts or url for link posts

```clojure
(creddit/submit creddit-client subreddit kind title content)
```

## Development

### Testing

Run tests

```sh
$ lein test
```

## Contributing

Want to become a Creddit [contributor](https://github.com/ThatGuyHughesy/creddit/blob/master/CONTRIBUTORS.md)?  
Then checkout our [code of conduct](https://github.com/ThatGuyHughesy/creddit/blob/master/CODE_OF_CONDUCT.md) and [contributing guidelines](https://github.com/ThatGuyHughesy/creddit/blob/master/CONTRIBUTING.md).

## Copyright & License

Copyright (c) 2017 Conor Hughes - Released under the MIT license.
