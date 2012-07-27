(defproject my-dictionary-client "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.4.0"]]
  :plugins[[lein-cljsbuild "0.2.4"]]
  :cljsbuild {
    :builds [{
      :source-path "src"
      :compiler {
        :output-to "main.js"
        :pretty-print true}}]})
