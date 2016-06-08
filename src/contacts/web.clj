(ns contacts.web
  (:require [bidi.bidi :refer [path-for]]
            [bidi.ring :refer [make-handler]]
            [contacts.domain :as domain]
            [liberator.core :refer [defresource]]
            [ring.middleware
             [absolute-redirects :refer [wrap-absolute-redirects]]
             [json :refer [wrap-json-body]]]))

(def routes ["/" {"" ::contacts
                  [:id] ::contact}])

(defresource contacts
  :allowed-methods [:get :post]
  :available-media-types ["application/json"]
  :handle-ok (fn [_]
               (domain/find-all-contacts))
  :post! (fn [ctx]
           (let [contact (get-in ctx [:request :body])]
             {::contact (domain/add-contact! contact)}))
  :post-redirect? (fn [{{id :id} ::contact}]
                    {:location (path-for routes ::contact :id id)}))

(defresource contact
  :allowed-methods [:get]
  :available-media-types ["application/json"]
  :exists? (fn [ctx]
             (let [id (get-in ctx [:request :params :id])]
               (if-let [contact (domain/find-contact-by-id id)]
                 {::contact contact})))
  :handle-ok (fn [ctx]
               (::contact ctx)))

(def handler
  (-> routes
      (make-handler {::contacts contacts
                     ::contact contact})
      (wrap-json-body {:keywords? true})
      (wrap-absolute-redirects)))
