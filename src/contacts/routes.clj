(ns contacts.routes
  (:require [bidi
             [bidi :as bidi]
             [ring :as ring]]))

(def routes ["/" {"" ::contacts
                  [:id] ::contact}])

(defn make-handler [route]
  (ring/make-handler routes route))

(defn path-for [handler & params]
  (apply (partial bidi/path-for routes handler) params))
