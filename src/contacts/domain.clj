(ns contacts.domain)

(defn- id []
  (str (java.util.UUID/randomUUID)))

(defn create-contact
  [firstname lastname email]
  {:id (id)
   :firstname firstname
   :lastname lastname
   :email email})

(def contacts
  (atom (into {}
              (map #(vector (:id %1) %1)
                   [(create-contact "Foo" "Bar" "foo@bar.org")
                    (create-contact "John" "Doe" "john@doe.com")
                    (create-contact "Jane" "Doe" "jane@doe.com")
                    (create-contact "Jake" nil "jake@doe.com")
                    (create-contact nil "Doe" "joel@doe.com")
                    (create-contact nil nil "jenna@doe.com")]))))

(defn add-contact! [contact]
  (let [id (id)
        contact (assoc contact :id id)]
    (swap! contacts assoc id contact)
    contact))

(defn find-all-contacts []
  (vals @contacts))

(defn find-contact-by-id [id]
  (get @contacts id))
