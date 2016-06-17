(ns contacts.config
  (:require [environ.core :refer [env]]))

(defn port []
  (Integer/parseInt (or (env :port) "3000")))
