(ns my-dictionary-client.core
  (:require [clojure.browser.event :as event]
            [clojure.browser.dom :as dom]
            [goog.net.XhrIo]
            [goog.ui.Container]
            [goog.ui.Control]
            [goog.debug.Logger :as logger]
            [goog.debug.Console :as console]))

(defn clj->js
  "Recursively transforms ClojureScript maps into Javascript objects,
   other ClojureScript colls into JavaScript arrays, and ClojureScript
   keywords into JavaScript strings."
  [x]
  (cond
   (string? x) x
   (keyword? x) (name x)
   (map? x) (.-strobj (reduce (fn [m [k v]]
                                (assoc m (clj->js k) (clj->js v))) {} x))
   (coll? x) (apply array (map clj->js x))
   :else x))

(.load js/google "visualization" "1.0" (clj->js {:packages ["corechart" "table"]}))

(defn- convert-to-table-data
  [entry]
  {:c [{:v (:pos entry)} {:v (:text entry)}]})

(defn- create-data
  [entries]
  (clj->js
   {:cols
    [
     {:id "pos" :label "POS" :type "string"}
     {:id "text":label "Meaning" :type "string"}]
    :rows (vec (for [entry entries] (convert-to-table-data entry)))}))
    
(def logger (logger/getLogger "cljstest.core"))

(console/autoInstall)

(defn- draw-table
  [entries]
  (.draw
   (js/google.visualization.Table. (dom/get-element :result))
   (js/google.visualization.DataTable. (create-data entries))))

(defn- dictionary-callback
  [e]
  (let [server-response (js->clj (.getResponseJson e/target) :keywordize-keys true)]
    (draw-table (:entries server-response))))

(defn- dictionary-request
  [e]
  (goog.net.XhrIo/send
   (str "http://localhost:3000/dictionary/" (dom/get-value (dom/get-element :query-word)))
   dictionary-callback))

(defn- init
  []
  (event/listen (dom/get-element :dictionary-button) "click" dictionary-request)
  nil)

(defn main []
  (.setOnLoadCallback js/google init))


