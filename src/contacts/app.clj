(ns contacts.app
  (:require [com.stuartsierra.component :as c]
            [system.components.jetty :refer [new-web-server]])
  (:gen-class))

(defn handler [req]
  {:status 200
   :body "Hello, world!"})

(defn new-system []
  (c/system-map
    :web (new-web-server 8080 handler)))

(defn -main [& args]
  (let [system (new-system)]
    (println "Starting HTTP server on port" (-> system :web :options :port))
    (c/start system)))
