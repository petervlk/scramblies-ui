(ns ^:figwheel-hooks vlk.scramblies-ui
  (:require
   [goog.dom :as gdom]
   [reagent.core :as reagent]
   [reagent.dom :as rdom]
   [vlk.components :as comps]))

;; define your app data so that it doesn't get over-written on reload
(defonce app-state (reagent/atom {:text "Hello world!"}))

(defn get-app-element []
  (gdom/getElement "app"))

(defn hello-world []
  [:div
   [:h1 (:text @app-state)]
   [:h3 "Edit this in src/vlk/scramblies_ui.cljs and watch it change!"]
   [comps/request-button "label"]
   [comps/scramble-form]]
  )

(defn mount [el]
  (rdom/render [comps/page] el))

(defn mount-app-element []
  (when-let [el (get-app-element)]
    (mount el)))

;; conditionally start your application based on the presence of an "app" element
;; this is particularly helpful for testing this ns without launching the app
(mount-app-element)

;; specify reload hook with ^;after-load metadata
(defn ^:after-load on-reload []
  (mount-app-element)
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)
