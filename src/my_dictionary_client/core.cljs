(ns my-dictionary-client.core
  (:require
   [clojure.browser.event :as event]
   [clojure.browser.dom :as dom]
   [clojure.browser.repl :as repl]
   [goog.net.XhrIo]
   [goog.debug.Logger :as logger]
   [goog.debug.Console :as console]
   [my-dictionary-client.util :as util]))

(def logger (logger/getLogger "my-dictionary-client.core"))

(defn- dictionary-callback
  [e]
  (dom/remove-children :result)
  (.draw
   (js/google.visualization.Table. (dom/get-element :result))
   (js/google.visualization.DataTable. (.getResponseJson e/target))))

(defn- dictionary-request
  [e]
  (goog.net.XhrIo/send
   (str
    "http://localhost:3000/dictionary/"
    (dom/get-value (dom/get-element :query-word)))
   dictionary-callback))

(defn- init
  []
  (event/listen (dom/get-element :dictionary-button) "click" dictionary-request)
  nil)

(repl/connect "http://localhost:9000/repl")
(console/autoInstall)
(.load js/google "visualization" "1.0" (util/clj->js {:packages ["corechart" "table"]}))   
(.setOnLoadCallback js/google init)