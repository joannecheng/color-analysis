(ns color-analysis-overview.core
  (:require [cljsjs.d3]
            [color-analysis-overview.data]
  ))

(enable-console-print!)

(def data (color-analysis-overview.data/values))
(def temp-data (nth data 150))

(def svg (.select js/d3 "#color_wheel"))

(def width 900)
(def height 300)
(def bar-width 4)

(.. svg (attr "width" width)
    (attr "height" height))

;(def max-val (reduce + (map #(% :value) temp-data)))

;(def bar-scale (.. js/d3
;                   (scaleLinear)
;                   (domain #js [1 max-val])
;                   (range #js [0.5 height])))

(def bars (.. svg
              (selectAll "g.bar-group")
              (data (clj->js data))
              (enter)
              (append "g")
              (classed "bar-group" true)
              (attr "transform" (fn [d i] (str "translate(" (* i bar-width) ")")))))


(println (clj->js (sort-by :color (temp-data :data))))

(def bar (.. bars
              (selectAll "rect.color-rect")
              (data #(clj->js (sort-by :color (.-data %))))
              (enter)
              (append "rect")
              (attr "class" (fn [d i] (str "color-rect-" i)))
              (attr "y" (fn [d i] (* i 10)))
              (attr "x" 0)
              (attr "height" 10)
              (attr "width" bar-width)
              (attr "fill" #(.-color %) )))
