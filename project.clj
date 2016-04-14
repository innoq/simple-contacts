(defproject contacts "0.1.0-SNAPSHOT"
  :dependencies [[compojure "1.5.0"]
                 [environ "1.0.2"]
                 [org.clojure/clojure "1.8.0"]
                 [org.clojure/data.xml "0.0.8"]
                 [ring/ring-core "1.4.0"]
                 [ring/ring-jetty-adapter "1.4.0"]
                 [ring/ring-json "0.4.0"]]
  :profiles {:uberjar {:aot [contacts.server]}}
  :main contacts.server
  :uberjar-name "simple-contacts.jar")
