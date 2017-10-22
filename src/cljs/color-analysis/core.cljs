(ns color-analysis.core
  (:require [cljsjs.d3]))

(enable-console-print!)

(set! (.-innerHTML (.getElementById js/document "app")) "<h1>Circles?</h1>")

(def values [
             { :color "#1e0821" :value 685 }
             { :color "#100211" :value 174 }
             { :color "#545876" :value 392 }
             { :color "#4f1141" :value 497 }
             { :color "#0a010d" :value 97  }
             { :color "#9cadb4" :value 278 }
             { :color "#190518" :value 333 }
             { :color "#100316" :value 232 }
             { :color "#a95a88" :value 386 }
             { :color "#241227" :value 517 }
             { :color "#331836" :value 585 }
             { :color "#a91255" :value 351 }
             { :color "#15051d" :value 218 }
             { :color "#781350" :value 489 }
             { :color "#2c082d" :value 574 }
             { :color "#090313" :value 132 }
          ])

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

(def circles (.. svg
                 (append "g")
                 (classed "circles-container" true)
    (selectAll "circle.circle-viz")))

(defn ticked []
  (def u (.. svg
             (selectAll "circle")
             (data (clj->js (.nodes simulation)))))
  (.. u
      (enter)
      (append "circle")
      (attr "fill" (fn [d] (.-color d)))
      (attr "r" #(radius-scale (.-value %)))
      (merge u)
      (attr "cx" #(.-x %))
      (attr "cy" #(.-y %)))

  (.. u (exit) (remove)))

(def simulation (.. js/d3
                    (forceSimulation (clj->js values))
                    (force "charge", (.strength (.forceManyBody js/d3) 5))
                    (force "center", (.forceCenter js/d3 300 250))
                    (force "collision", (.radius (.forceCollide js/d3) #(radius-scale (.-value %))))
                    (on "tick" ticked))) ; todo: only tick 100 times
