(defproject ck.mailer "0.1.0-SNAPSHOT"
  :description "FIXME: ck.mailer module for Conskit"
  :url "https://website.com/example/ck.mailer"
  :license {:name "Apache License, Version 2.0"
            :url "http://www.apache.org/licenses/LICENSE-2.0.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [conskit "0.2.0-SNAPSHOT"]
                 [clojurewerkz/mailer "1.3.0"]]
  :profiles {:dev {:source-paths ["dev"]
                   :resource-paths ["dev-resources"]
                   :dependencies [[puppetlabs/trapperkeeper "1.4.1" :classifier "test"]
                                  [puppetlabs/kitchensink "1.3.1" :classifier "test" :scope "test"]
                                  [midje "1.8.3"]]
                   :plugins [[lein-midje "3.2"]]}})
