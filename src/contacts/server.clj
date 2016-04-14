(ns contacts.server
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [contacts.core :as core])
  (:gen-class))

(defn -main [& args]
  (run-jetty
    core/webapp
    {:port 8080
     :join? false}))
