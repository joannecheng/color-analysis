(defproject color-analysis "0.1.0-SNAPSHOT"
  :license {:name "Mozilla Public License 2.0" :url "https://www.mozilla.org/en-US/MPL/2.0/"}

  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.9.229"]
                 [org.clojure/core.async "0.1.346.0-17112a-alpha"]
                 [cljsjs/d3 "4.2.2-0"]
                 [figwheel-sidecar "0.5.14"]
                 [com.cemerick/piggieback "0.2.2"]]

  :source-paths ["src/clj"]

  :plugins [[lein-cljsbuild "1.1.4"]
            [lein-figwheel "0.5.14"]
            [cider/cider-nrepl "0.16.0"]]

  ;; Start Figwheel as soon as the process starts
  :injections [(use 'figwheel-sidecar.repl-api)
               (start-figwheel!)]

  ;; Add the piggieback middleware to nREPL, this allows nREPL to work with
  ;; ClojureScript
  ;; In Vim:
  ;; :Piggieback (figwheel-sidecar.repl-api/repl-env)
  :repl-options {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}

  :clean-targets ^{:protect false} [:target-path :compile-path "resources/public/js/compiled"]

  :cljsbuild {:builds
              {:dev {:source-paths ["src/cljs"]
                     :figwheel true
                     :compiler {:main color-analysis.core
                                :optimizations :none
                                :output-to "resources/public/js/compiled/color-analysis.js"
                                :output-dir "resources/public/js/compiled/dev"
                                :asset-path "js/compiled/dev"
                                :source-map-timestamp true}}

               :prod {:source-paths ["src/cljs"]
                      :jar true
                      :compiler {:main color-analysis.core
                                 :optimizations :advanced
                                 :output-to "resources/public/js/compiled/color-analysis.js"
                                 :source-map "resources/public/js/compiled/color-analysis.js.map"
                                 :output-dir "resources/public/js/compiled/prod"
                                 :source-map-timestamp true
                                 :pretty-print false}}}})
