(ns contacts.server
  (:require [contacts.core :as core]
            [environ.core :refer [env]]
            [ring.adapter.jetty :refer [run-jetty]])
  (:gen-class))

(defn- port []
  (Integer/parseInt (or (env :port) "3000")))

(defn -main [& args]
  (run-jetty
    core/webapp
    {:port (port)
     :join? false}))
