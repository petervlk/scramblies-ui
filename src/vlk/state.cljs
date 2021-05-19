(ns vlk.state
  (:require
    [reagent.core :as reagent]))

(def clean-form-data
  {:scramble ""
   :target   ""
   :response ""})

(def doc (reagent/atom clean-form-data))

(defn request-params [doc]
  {:target   (:target @doc)
   :scramble (:scramble @doc)})

(defn scramble-response [doc]
  (:response @doc))

(defn no-response? [doc]
  (empty? (scramble-response doc)))

