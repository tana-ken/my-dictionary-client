(ns my-dictionary-client.core
  (:require
   [clojure.browser.event :as event]
   [clojure.browser.dom :as dom]
   [clojure.browser.repl :as repl]
   [goog.net.XhrIo]
   [goog.debug.Logger :as logger]
   [goog.debug.Console :as console]
   [my-dictionary-client.util :as util]))

(repl/connect "http://localhost:9000/repl")

(.load js/google "visualization" "1.0" (util/clj->js {:packages ["corechart" "table"]}))
    
(def logger (logger/getLogger "cljstest.core"))

(console/autoInstall)

(defn- draw-table
  [server-response]
  (.draw
   (js/google.visualization.Table. (dom/get-element :result))
   (js/google.visualization.DataTable. server-response)))

(defn- dictionary-callback
  [e]
  (draw-table (.getResponseJson e/target)))

(defn- dictionary-request
  [e]
  (goog.net.XhrIo/send
  (str "http://localhost:3000/dictionary/" (dom/get-value (dom/get-element :query-word)))
   dictionary-callback))

(defn- init
  []
  (event/listen (dom/get-element :dictionary-button) "click" dictionary-request)
  nil)


(.setOnLoadCallback js/google init)