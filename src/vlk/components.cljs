(ns vlk.components
  (:require
    [reagent.core :as reagent]
    [ajax.core :refer [POST]]
    [reagent-forms.core :refer [bind-fields init-field value-of]]))

(defn invalid? [s]
  (nil? (re-matches #"^[a-z]+$" s)))

(defn row [label input]
  [:div.row
   [:div.col-md-2 [:label label]]
   [:div.col-md-5 input]])

(defn input [label type id]
  (row label [:input.form-control {:field type :id id}]))

(def form-template
  [:div
   (input "scramble" :text :scramble)
   [:div.row
    [:div.col-md-2]
    [:div.col-md-5
     [:div.alert.alert-success
      {:field :alert :id :scramble :event invalid?}
      "scramble value is invalid!"]
     ;[:div.alert.alert-danger
     ; {:field :alert :id :errors.scramble}]
     ]]
   (input "target" :text :target)
   [:div.row
    [:div.col-md-2]
    [:div.col-md-5
     [:div.alert.alert-success
      {:field :alert :id :target :event invalid?}
      "target value is invalid!"]
     ;[:div.alert.alert-danger
     ; {:field :alert :id :errors.target}]
     ]]])

(defn page []
  (let [doc (reagent/atom {:scramble ""
                           :target   ""})]
    (fn []
      [:div
       [:div.page-header [:h1 "Scramblies Form"]]
       [bind-fields form-template doc]
       [:button.btn.btn-default
        {:on-click
         (fn display-errors [_]
           (do (if (invalid? (get-in @doc [:scramble]))
                 (swap! doc assoc-in [:errors :scramble] "scramble value is not valid"))
               (if (invalid? (get-in @doc [:target]))
                 (swap! doc assoc-in [:errors :target] "target valur is not valid"))))}
        "save"]])))


(defn scramble-input [name form-data]
  [:input {:type      "text"
           :name      name
           :on-change #(swap! form-data assoc name (-> % .-target .-value))}])

(defn request-button [label form-data]
  (let [resp (reagent/atom "RESP-VLKO")]
    [:div
     [:button
      {:on-click
       (fn [_]
         (.log js/console @resp)
         #_(POST
             "http://localhost:4000"
             {:params          {:target (:target @form-data) :scramble (:scramble @form-data)}
              :response-format :json
              :handler         (fn [response]
                                 (reset! resp (str response))
                                 )})

         )}
      label]
     [:p @resp]]))

(defn scramble-form []
  (let [form-data (reagent/atom {:target "" :scramble ""})]
    (fn []
      [:form {:on-submit (fn [e] (.preventDefault e))}
       [scramble-input :scramble form-data]
       [scramble-input :target form-data]
       [request-button "click"]
       [:p (:resp @form-data)]])))

