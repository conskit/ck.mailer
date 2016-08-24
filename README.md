# ck.mailer [![Build Status](https://travis-ci.org/conskit/ck.mailer.svg?branch=master)](https://travis-ci.org/conskit/ck.mailer) [![Dependencies Status](https://jarkeeper.com/conskit/ck.mailer/status.svg)](https://jarkeeper.com/conskit/ck.mailer) [![Clojars Project](https://img.shields.io/clojars/v/ck.mailer.svg)](https://clojars.org/ck.mailer)

Emailing module for [conskit](https://github.com/conskit/conskit) based on [clojurewerkz/mailer](https://github.com/clojurewerkz/mailer)

## Installation
Add the dependency in the clojars badge above in your `project.clj`.

## Usage
Add the following to your `bootstrap.cfg`:

```
ck.react-server/renderer
```

Add the following to your `config.conf`

```properties
email: {
  delivery-mode: smtp # or test
  host: "smtp.gmail.com" # mail server
  user: "email.address@gmail.com" 
  pass: "realpassword"
  ssl: true
  templateDir: "email/templates/" #template directory
  constants: {
    # values available globally to all templates
    title: "My Application"
  }
}

```

Add the dependency and binding in your serivice

```clojure
(ns myapp
  (:require [ck.react-server :as ckrs]))

(defservice
  my-service
  [[:ActionRegistry register-bindings!]
   [:CKMailer send-email!]]
  (init [this context]
    ...
    (register-bindings {:send-email! send-email!}))
  ...)
```

Create a template file: `hello-template.mustache` in your email directory (e.g. `email/templates`) which should be created somewhere on your classpath (typically in the `resources` directory):

```mustache
<h1>{{title}}</h1>
<p>
    Hello From <b>{{name}}</b>!
</p>
```

Now you should be able to use the binding in your actions:

```clojure
(defcontroller
  mycontroller
  [send-email!]
  
  (action
    hello-from-email
    [req]
    (send-email! "foo@bar.com" "Important Subject" :hello-template {:name "Conskit"})))
```

## License

Copyright © 2016 Jason Murphy

Distributed under the [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html).
