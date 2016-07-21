(ns ck.mailer
  (:require
    [clojure.tools.logging :as log]
    [puppetlabs.trapperkeeper.core :refer [defservice]]
    [puppetlabs.trapperkeeper.services :refer [service-context]]
    [clojurewerkz.mailer.core :refer [deliver-email with-settings with-delivery-mode]]))

(defn when-done [future-to-watch function-to-call]
  (future (function-to-call @future-to-watch)))

(defprotocol CKMailer
  "Service to handle emails"
  (send-email! [this to subject template values] "Send an email"))

(defservice
  mailer CKMailer
  [[:ConfigService get-in-config]]
  (send-email! [this to subject template values]
               (log/info "Sending Email...")
               (let [p (promise)
                     {:keys [delivery-mode from-address template-dir constants] :as settings} (get-in-config [:email])]
                 (when-done
                   (future (with-settings
                             settings
                             (with-delivery-mode
                               (keyword delivery-mode)
                               (deliver-email {:from    from-address
                                               :to      to
                                               :subject subject}
                                              (str template-dir (name template) ".mustache")
                                              (merge constants values)
                                              :text/html))))
                   #(deliver p %))
                 p)))
