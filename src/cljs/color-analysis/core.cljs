(ns color-analysis.core
  (:require [cljsjs.d3]
            [color-analysis.data]))


(enable-console-print!)
(declare simulation)

;(set! (.-innerHTML (.getElementById js/document "app")) "<h1>Circles?</h1>")

(def color-data (color-analysis.data/values))

(def radius-scale (.. js/d3
                      (scaleSqrt)
                      (domain #js [1 900])
                      (range #js [1 20])))

(def counter (atom 1))
(defn next-value [] (swap! counter inc))

(defn ticked [container-number simulation]
  (def container-string (str "svg[data-scene-number=\"" container-number "\"]"))
  (def container (clj->js (.select js/d3 container-string)))

  (let [circles (.. container
                    (selectAll "circle")
                    (data (clj->js (.nodes simulation))))]

    (.. circles
        (enter)
        (append "circle")
        (attr "fill" (fn [d] (.-color d)))
        (attr "r" #(radius-scale (.-value %)))
        (merge circles)
        (attr "cx" #(.-x %))
        (attr "cy" #(.-y %)))

    (.. circles (exit) (remove)) ))

(doseq [item color-data]
  (let [simulation
        (.. js/d3
            (forceSimulation (clj->js (item :values)))
            (force "charge", (.strength (.forceManyBody js/d3) 6))
            (force "center", (.forceCenter js/d3 58 65))
            (force "collision",
                   (.radius (.forceCollide js/d3) #(radius-scale (.-value %)))))]

    (.. simulation (on "tick" #(ticked (item :name) simulation))) ))
