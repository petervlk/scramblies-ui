(ns vlk.page
  (:require
    [reagent-forms.core :refer [bind-fields init-field value-of]]
    [vlk.validator :as v]
    [vlk.handler :as h]
    [vlk.state :as state :refer [doc]]))

(defn row [label-node content-node]
  [:div.row
   [:div.col-md-2 label-node]
   [:div.col-md-10 content-node]])

(defn row-labeled [label content]
  (row [:label label] content))

(defn row-unlabeled [content]
  (row nil content))

(defn row-input [label type id]
  (row-labeled
    label
    [:input.form-control {:field type :id id :autocomplete "off"}]))

(defn row-input-validation [node]
  (row-unlabeled node))

(defn validation-node [id validator msg]
  [:div.alert.alert-success
   {:field :alert :id id :event validator}
   msg])

(def form-template
  [:div
   (row-input "Scramble" :text :scramble)
   (row-input-validation
     (validation-node :scramble v/invalid? v/valid-value-description))
   (row-input "Target word" :text :target)
   (row-input-validation
     (validation-node :target v/invalid? v/valid-value-description))])

(defn page-form [doc]
  [:div
   [:div.page-header [:h1 "Scramblies Form"]]
   [bind-fields form-template doc]
   [:div.row
    [:div.col-md-12
     [:button.btn.btn-default
      {:on-click (h/valid-request-sender
                   (state/request-params doc)
                   (h/response-setter doc))}
      "Scrambled?"]]]])

(defn page-result [doc]
  [:div
   [:div.page-header [:h1 "Scramblies Result"]]
   [row-labeled "Result:" (state/scramble-response doc)]
   [:div.row
    [:div.col-md-12
     [:button.btn.btn-default
      {:on-click (h/data-setter doc state/clean-form-data)}
      "Back to form"]]]])

(defn page []
  (if (state/no-response? doc)
    (page-form doc)
    (page-result doc)))