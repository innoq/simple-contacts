(defproject contacts "0.1.0-SNAPSHOT"
  :url "https://github.com/innoq/simple-contacts"
  :description "A simple application which stores contacts using an event
               store."
  :license {:name "Apache License, Version 2.0"
            :url "http://www.apache.org/licenses/LICENSE-2.0"
            :distribution :repo
            :comments "A business-friendly OSS license"}
  :min-lein-version "2.6.1"
  :dependencies [[bidi "2.0.9"]
                 [com.stuartsierra/component "0.3.1"]
                 [environ "1.0.3"]
                 [hiccup "1.0.5"]
                 [liberator "0.14.1"]
                 [org.clojure/clojure "1.8.0"]
                 [org.clojure/data.json "0.2.6"]
                 [org.danielsz/system "0.3.0"]
                 [ring/ring-core "1.5.0"]
                 [ring/ring-headers "0.2.0"]
                 [ring/ring-jetty-adapter "1.5.0"]
                 [ring/ring-json "0.4.0"]

                 ;[compojure "1.5.0"]
                 ;[org.clojure/data.xml "0.0.8"]
                 ]
  :main contacts.app
  :uberjar-name "simple-contacts.jar"
  :profiles {:dev {:dependencies [[reloaded.repl "0.2.2"]]
                   :repl-options {:init-ns user}
                   :source-paths ["dev"]}
             :uberjar {:aot [contacts.app]}})
