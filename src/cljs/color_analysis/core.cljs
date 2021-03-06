(ns color-analysis.core
  (:require [cljsjs.d3]
            [color-analysis.dna-data]
            [color-analysis.data]
            [color-analysis-overview.core]))


(enable-console-print!)
(declare simulation)

(def color-data (color-analysis.data/values))
(def dna-color-data (color-analysis.dna-data/values))

(def radius-scale (.. js/d3
                      (scaleSqrt)
                      (domain #js [1 1313])
                      (range #js [0.5 18])))

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

(defn draw-collection [collection]
  (doseq [item collection]
    (let [simulation
          (.. js/d3
              (forceSimulation (clj->js (item :values)))
              (force "charge", (.strength (.forceManyBody js/d3) 6))
              (force "center", (.forceCenter js/d3 58 65))
              (force "collision",
                    (.radius (.forceCollide js/d3) #(radius-scale (.-value %)))))]

      (.. simulation (on "tick" #(ticked (item :name) simulation))) )))

(draw-collection color-data)
(draw-collection dna-color-data)
