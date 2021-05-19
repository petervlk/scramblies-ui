(ns vlk.validator)

(def valid-value-description
  "Value must be a non-empty string containing only lowercase letters!")

(defn invalid? [string-value]
  (nil? (re-matches #"^[a-z]+$" string-value)))

(defn valid-request-data? [request-data]
  (not-any? invalid? (vals request-data)))
