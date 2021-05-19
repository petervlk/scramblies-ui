(ns vlk.handler
  (:require
    [ajax.core :refer [POST]]
    [vlk.validator :as v]))

(defn data-setter [doc data]
  (fn [_] (reset! doc data)))

(defn response-setter [doc]
  (fn [response] (swap! doc assoc :response (str response))))

(defn error-handler [{:keys [status status-text]}]
  (js/alert (str "Response status: " status "; "
                 "Response value: " status-text)))

(defn send-request [request-data response-handler]
  ; TODO - refactor
  (POST
    "http://localhost:4000"
    {:params          request-data
     :response-format :json
     :handler         response-handler
     :error-handler   error-handler
     :timeout         2000}))

(defn valid-request-sender [request-data response-handler]
  (fn [_]
    (when (v/valid-request-data? request-data)
      (send-request request-data response-handler))))

