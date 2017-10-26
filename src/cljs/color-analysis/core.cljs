(ns color-analysis.core
  (:require [cljsjs.d3]
            [color-analysis.data]))


(enable-console-print!)
(declare simulation)

;(set! (.-innerHTML (.getElementById js/document "app")) "<h1>Circles?</h1>")

(def values (get (nth (color-analysis.data/values) 1) :values))

(def width 960)
(def height 500)

(def radius-scale (.. js/d3
                      (scaleSqrt)
                      (domain #js [1 900])
                      (range #js [1 30])))

(def svg (.. js/d3
             (select "#app")
             (append "svg")
             (attr "width" width)
             (attr "height" height)))


(defn ticked []
  ;change to let?
  (def circles (.. svg
             (selectAll "circle")
             (data (clj->js (.nodes simulation)))))
  (.. circles
      (enter)
      (append "circle")
      (attr "fill" (fn [d] (.-color d)))
      (attr "r" #(radius-scale (.-value %)))
      (merge circles)
      (attr "cx" #(.-x %))
      (attr "cy" #(.-y %)))

  (.. circles (exit) (remove)))

(def simulation (.. js/d3
                    (forceSimulation (clj->js values))
                    (force "charge", (.strength (.forceManyBody js/d3) 5))
                    (force "center", (.forceCenter js/d3 300 250))
                    (force "collision", (.radius (.forceCollide js/d3) #(radius-scale (.-value %))))
                    (on "tick" ticked))) ; todo: only tick 100 times
