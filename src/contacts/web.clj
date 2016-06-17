(ns contacts.web
  (:require [contacts.domain :as domain]
            [contacts.html :as html]
            [contacts.routes :as routes]
            [liberator.core :refer [defresource]]
            [ring.middleware
             [absolute-redirects :refer [wrap-absolute-redirects]]
             [json :refer [wrap-json-body]]]))

(defn- media-type [ctx]
  (get-in ctx [:representation :media-type]))

(defresource contacts
  :allowed-methods [:get :post]
  :available-media-types ["text/html" "application/json"]
  :handle-ok (fn [ctx]
               (let [contacts (domain/find-all-contacts)]
                 (condp = (media-type ctx)
                   "text/html" (html/contacts contacts)
                   "application/json" contacts)))
  :post! (fn [ctx]
           (let [contact (get-in ctx [:request :body])]
             {::contact (domain/add-contact! contact)}))
  :post-redirect? (fn [{{id :id} ::contact}]
                    {:location (routes/path-for ::routes/contact :id id)}))

(defresource contact
  :allowed-methods [:get]
  :available-media-types ["text/html" "application/json"]
  :exists? (fn [ctx]
             (let [id (get-in ctx [:request :params :id])]
               (if-let [contact (domain/find-contact-by-id id)]
                 {::contact contact})))
  :handle-ok (fn [{contact ::contact, :as ctx}]
               (condp = (media-type ctx)
                 "text/html" (html/contact contact)
                 "application/json" contact)))

(def handler
  (-> (routes/make-handler {::routes/contacts contacts
                            ::routes/contact contact})
      (wrap-json-body {:keywords? true})
      (wrap-absolute-redirects)))
