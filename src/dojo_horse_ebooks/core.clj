(ns dojo-horse-ebooks.core
  (:require [clojure.string :refer [split]]
            [compojure.route :as route]
            [ring.adapter.jetty :as jetty])
  (:use compojure.core))

(defn get-corpus [url]
  (slurp (java.net.URI. url)))

(def corpus (get-corpus "http://www.gutenberg.org/cache/epub/1661/pg1661.txt"))



(defn corpus->wordlist [corpus]
  (split corpus #"\s+"))

(defn get-some [offset amount word-seq]
  (take amount (drop offset word-seq)))

(defn get-random-words [corpus]
  (let [wl (corpus->wordlist corpus)]
    (clojure.string/join " " 
(get-some (rand-int (count wl)) 17 wl))))


(defroutes app
  (GET "/" [] (apply str "<h1 style='text-align: center; margin-top: 100px' >" (get-random-words corpus) "</h1>"))
  (route/not-found "<h1>Page not found</h1>"))

(defn -main []
  (jetty/run-jetty #'app {:port 8912}))
