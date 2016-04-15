(ns user
  (:require [clojure.java.io :as io]
            [clojure.pprint :refer [pprint]]
            [clojure.repl :refer :all]
            [clojure.tools.namespace.repl :refer [refresh]]
            [com.stuartsierra.component :as component]
            [reloaded.repl :refer [system init start stop go reset]]
            [contacts.app :as app]))

(reloaded.repl/set-init! app/new-system)
