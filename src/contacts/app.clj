(ns contacts.app
  (:require [com.stuartsierra.component :as c]
            [contacts.config :as config]
            [contacts.web :as web]
            [system.components.jetty :refer [new-web-server]])
  (:gen-class))

(defn new-system []
  (c/system-map
    :web (new-web-server (config/port) web/handler)))

(defn -main [& args]
  (let [system (new-system)]
    (println "Starting HTTP server on port" (-> system :web :options :port))
    (c/start system)))
