(ns ^:figwheel-hooks vlk.scramblies-ui
  (:require
    [goog.dom :as gdom]
    [reagent.dom :as rdom]
    [vlk.page :refer [page]]))

(defn get-app-element []
  (gdom/getElement "app"))

(defn mount [el]
  (rdom/render [vlk.page/page] el))

(defn mount-app-element []
  (when-let [el (get-app-element)]
    (mount el)))

;; conditionally start your application based on the presence of an "app" element
;; this is particularly helpful for testing this ns without launching the app
(mount-app-element)

;; specify reload hook with ^;after-load metadata
(defn ^:after-load on-reload []
  (mount-app-element))
