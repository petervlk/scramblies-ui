(ns vlk.components
  (:require
    [reagent.core :as reagent]
    [ajax.core :refer [POST]]
    [reagent-forms.core :refer [bind-fields init-field value-of]]))

(def valid-value-description
  "Value must be a non-empty string containing only lowercase letters!")

(defn invalid? [s]
  (nil? (re-matches #"^[a-z]+$" s)))

(defn row [label-node content-node]
  [:div.row
   [:div.col-md-2 label-node]
   [:div.col-md-10 content-node]])

(defn row-labeled [label content]
  (row [:label label] content))

(defn row-unlabeled [content]
  (row nil content))

(defn input [label type id]
  (row-labeled
    label
    [:input.form-control {:field type :id id :autocomplete "off"}]))

(defn input-validation [node]
  (row-unlabeled node))

(defn validation-node [id validator msg]
  [:div.alert.alert-success
   {:field :alert :id id :event validator}
   msg])

(def form-template
  [:div
   (input "Scramble" :text :scramble)
   (input-validation
     (validation-node :scramble invalid? valid-value-description))
   (input "Target word" :text :target)
   (input-validation
     (validation-node :target invalid? valid-value-description))])

(defn page []
  (let [doc (reagent/atom {:scramble ""
                           :target   ""})]
    (fn []
      [:div
       [:div.page-header [:h1 "Scramblies Form"]]
       [bind-fields form-template doc]
       [:button.btn.btn-default
        {:on-click
         (fn [_]
           (when
             (not-any? #(invalid? (% @doc)) [:scramble :target])
             (POST
               "http://localhost:4000"
               {:params          {:target   (:target @doc)
                                  :scramble (:scramble @doc)}
                :response-format :json
                :handler         (fn [response] (js/alert (str response)))})
             ))
         }
        "Scrambled?"]])))
