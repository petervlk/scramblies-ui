;; This test runner is intended to be run from the command line
(ns vlk.test-runner
  (:require
    ;; require all the namespaces that you want to test
    [vlk.scramblies-ui-test]
    [figwheel.main.testing :refer [run-tests-async]]))

(defn -main [& args]
  (run-tests-async 5000))
