(ns contacts.core
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
            [cheshire.core :as json]
            [clojure.data.xml :as xml]))

(defmulti handle-event (fn [entity event]
                         (:type event)))

(defmethod handle-event :contact-created
  [_ event]
  (:payload event))

(defmethod handle-event :contact-updated
  [contact event]
  (apply conj contact (-> event :payload :updates)))

(defn id []
  (str (java.util.UUID/randomUUID)))

(defn event [type payload]
  {:type type
   :payload payload
   :id (id)
   :timestamp (.getTime (java.util.Date.))})

(defn events [& evts]
  evts)

(defn apply-events [item events]
  (reduce handle-event item events))

(defn contact-url [contact request]
  (str (name (:scheme request)) "://" (get (:headers request) "host") "/contacts/" (:id contact)))

(defn new-contact [firstname lastname email]
  {:firstname firstname
   :lastname lastname
   :email email
   :id (id)})

(defn store-contact [contact request]
  (events (event :contact-created (assoc contact :self (contact-url contact request)))))

(defn update-contact [contact updates request]
  (events (event :contact-updated {:id (:id contact)
                                   :updates updates
                                   :contact (contact-url contact request)})))

(defprotocol EventStore
  (persist! [this id events])
  (events-for [this id])
  (watch! [this handler]))

(defrecord InMemoryEventStore [events-atom watchers-atom]
  EventStore
  (persist! [_ id events]
    (swap! events-atom (fn [current]
                         (let [existing-events (get current id [])]
                           (assoc current id (into existing-events events)))))
    (doseq [handler @watchers-atom
            event events]
      (handler event)))
  (events-for [_ id]
    (@events-atom id))
  (watch! [_ handler]
    (swap! watchers-atom conj handler)))

(defn in-memory-event-store []
  (InMemoryEventStore. (atom {}) (atom [])))

(defn create-contact! [event-store firstname lastname email request]
  (let [contact (new-contact firstname lastname email)
        events (store-contact contact request)]
    (persist! event-store (:id contact) events)
    contact))

(defn load-contact [event-store id]
  (let [events (events-for event-store id)]
    (apply-events {} events)))

(defn update-contact! [event-store id updates request]
  (let [contact (load-contact event-store id)
        events (update-contact contact updates request)]
    (if-not (empty? contact)
      (do 
        (persist! event-store id events)
        true)
      false)))

(defn entry [event]
  [:entry
   [:title (-> event :type name)]
   [:updated (:timestamp event)]
   [:author [:name "contacts service"]]
   [:id (str "urn:contacts:feed:event:" (:id event))]
   [:content {:type "json"} (json/generate-string event)]])

(defn feed-url [request]
  (str (name (:scheme request)) "://" (get (:headers request) "host") "/feed"))

(defn atom-feed [events url]
  (xml/emit-str
   (xml/sexp-as-element
    [:feed {:xmlns "http://www.w3.org/2005/Atom"}
     [:id "urn:contacts:feed"]
     [:updated (-> events last :timestamp)]
     [:title {:type "text"} "contacts events"]
     [:link {:rel "self" :href url}]
     (map entry events)])))

(defn app-routes [event-store feed-items]
  (routes
   (POST "/contacts" request
         (let [{:keys [firstname lastname email]} (:body request)
               contact (create-contact! event-store firstname lastname email request)]
           {:status 201
            :headers {"Location" (contact-url contact request)}}))
   (PUT "/contacts/:id" [id :as request]
        (if (update-contact! event-store id (:body request) request)
          {:status 200}
          {:status 404}))
   (GET "/contacts/:id" [id :as request]
        (when-let [contact (load-contact event-store id)]
          {:status 200
           :body contact}))
   (GET "/feed" request
        {:status 200
         :headers {"Content-Type" "application/atom+xml"}
         :body (atom-feed (reverse @feed-items) (feed-url request))})))
  
(defn latest-items [count holder]
  (fn [item]
    (swap! holder (fn [items]
                    (->> (conj items item)
                         (take count)
                         vec)))))

(def webapp
  (let [event-store (in-memory-event-store)
        feed-items (atom [])]
    (watch! event-store (latest-items 20 feed-items))
    (-> (app-routes event-store feed-items)
        wrap-json-response
        (wrap-json-body {:keywords? true}))))
