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
     (validation-node :scramble invalid? valid-value-description))
   (row-input "Target word" :text :target)
   (row-input-validation
     (validation-node :target invalid? valid-value-description))])

(defn response-row [doc]
  (let [response (:response @doc)]
    ((when response
       [:div
        [:hr]
        [:div.row
         [:div.col
          response]]]))))

(def clean-form-data
  {:scramble ""
   :target   ""
   :response ""})

(def response-form-data
  {:scramble ""
   :target   ""
   :response "vlko was here!"})

(def doc (reagent/atom clean-form-data))

(defn page-form [doc]
  [:div
   [:div.page-header [:h1 "Scramblies Form"]]
   [bind-fields form-template doc]
   [:div.row
    [:div.col-md-12
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
                :handler         (fn [response]
                                   (swap! doc assoc :response (str response))
                                   )})))}
      "Scrambled?"]]]])

(defn page-result [doc]
  [:div
   [:div.page-header [:h1 "Scramblies Result"]]
   [row-labeled "Result:" (:response @doc)]
   [:div.row
    [:div.col-md-12
     [:button.btn.btn-default
      {:on-click
       (fn [_]
         (reset! doc clean-form-data))}
      "Back to form"]]]])


(defn page []
  (if (= "" (:response @doc)) (page-form doc) (page-result doc)))