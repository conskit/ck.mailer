(ns ck.mailer-test
  (:require
    [puppetlabs.trapperkeeper.app :as app]
    [puppetlabs.trapperkeeper.core :refer [defservice]]
    [puppetlabs.trapperkeeper.services :refer [service-context]]
    [puppetlabs.trapperkeeper.testutils.bootstrap :refer [with-app-with-cli-data]]
    [ck.mailer :refer [mailer]]
    [clojurewerkz.mailer.core :refer [reset-deliveries!]]
    [conskit.core :as ck]
    [conskit.macros :refer :all])
  (:use midje.sweet))

(defprotocol ResultService
  (get-result [this]))

(defservice
  test-service ResultService
  [[:CKMailer send-email!]]
  (init [this context]
        context)
  (start [this context]
         {:result (send-email!
                    "foo@bar.com"
                    "Testing email"
                    :hello
                    {:name "Jane Doe"})})
  (stop [this context]
        (reset-deliveries!)
        context)
  (get-result [this]
              (:result (service-context this))))

(with-app-with-cli-data
  app
  [mailer test-service]
  {:config "./dev-resources/test-config.conf"}
  (let [serv (app/get-service app :ResultService)
        res (get-result serv)]
    (fact @res => [{:body [{:content "<p>\n    Hello, <b>Jane Doe</b>!\n</p>\n", :type "text/html"}],
                    :from "John Doe <john@doe.com>",
                    :subject "Testing email",
                    :to "foo@bar.com"}])))

