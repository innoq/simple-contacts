(ns contacts.web
  (:require [bidi.ring :refer [make-handler]]
            [liberator.core :refer [defresource]]))

(defresource root
  :available-media-types ["text/plain"]
  :handle-ok "Hello, world!")

(def routes ["/" { "" root}])

(def handler
  (make-handler routes))
