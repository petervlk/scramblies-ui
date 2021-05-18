(ns vlk.components
  (:require
    [reagent.core :as reagent]
    [ajax.core :refer [POST]]
    [reagent-forms.core :refer [bind-fields init-field value-of]]))

(def valid-value-description
  "Value must be a non-empty string containing only lowercase letters!")

(defn invalid? [s]
  (nil? (re-matches #"^[a-z]+$" s)))

(def clean-form-data
  {:scramble ""
   :target   ""
   :response ""})

(def doc (reagent/atom clean-form-data))

(defn set-data-handler [doc data]
  (fn [_] (reset! doc data)))

(defn set-response-value-handler [doc]
  (fn [response] (swap! doc assoc :response (str response))))

(defn request-params [doc]
  {:target   (:target @doc)
   :scramble (:scramble @doc)})

(defn scramble-response [doc]
  (:response @doc))

(defn no-response? [doc]
  (empty? (scramble-response doc)))

(defn valid-request-data? [request-data]
  (not-any? invalid? (vals request-data)))

(defn error-handler [{:keys [status status-text]}]
  (js/alert (str "Response status: " status "; "
                 "Response value: " status-text)))

(defn send-request [request-params response-handler]
  (POST
    "http://localhost:4000"
    {:params          request-params
     :response-format :json
     :handler         response-handler
     :error-handler   error-handler
     :timeout         2000}))

(defn validate-and-send-request [doc]
  (let [request-data (request-params doc)]
    (fn [_]
      (when (valid-request-data? request-data)
        (send-request request-data (set-response-value-handler doc))))))

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

(defn page-form [doc]
  [:div
   [:div.page-header [:h1 "Scramblies Form"]]
   [bind-fields form-template doc]
   [:div.row
    [:div.col-md-12
     [:button.btn.btn-default
      {:on-click (validate-and-send-request doc)}
      "Scrambled?"]]]])

(defn page-result [doc]
  [:div
   [:div.page-header [:h1 "Scramblies Result"]]
   [row-labeled "Result:" (scramble-response doc)]
   [:div.row
    [:div.col-md-12
     [:button.btn.btn-default
      {:on-click (set-data-handler doc clean-form-data)}
      "Back to form"]]]])

(defn page []
  (if (no-response? doc)
    (page-form doc)
    (page-result doc)))