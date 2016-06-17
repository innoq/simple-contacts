(ns contacts.html
  (:require [contacts.routes :as routes]
            [hiccup
             [core :refer [h]]
             [element :refer [link-to]]
             [page :refer [html5]]]))

(defn- contact-name [{firstname :firstname, lastname :lastname, email :email}]
  (if firstname
    (if lastname
      (str firstname " " lastname)
      firstname)
    (if lastname
      lastname
      email)))

(defn- contact-email [{email :email, :as contact}]
  (str (contact-name contact) " <" email ">"))

(defn contacts [contacts]
  (html5
     [:head
      [:meta {:charset "utf-8"}]
      [:title "simple-contacts"]]
     [:body
      [:main
      [:h1 "Contacts"]
      [:ul
       (map #(vector :li (link-to (routes/path-for ::routes/contact
                                            :id (:id %))
                                  (h (contact-email %)))) contacts)]]]))

(defn contact [contact]
  (html5
     [:head
      [:meta {:charset "utf-8"}]
      [:title "simple-contacts"]]
     [:body
      [:main
       [:h1 (h (contact-email contact))]
       (link-to (routes/path-for ::routes/contacts) "Back")]]))
