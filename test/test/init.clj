(ns test.init
  (:import [java.io File])
  (:require [config.db-config :as db-config]
            [drift.runner :as drift-runner]
            [masques.core :as masques-core]))

(def test-init? (atom false)) 

(defn
  init-tests []
  (when (compare-and-set! test-init? false true)
    (println "Initializing test database.")
    (masques-core/set-mode "test")
    (db-config/update-username-password "test" "password")
    (masques-core/database-init)
    (drift-runner/update-to-version 0) ; Reset the test database
    (drift-runner/update-to-version Long/MAX_VALUE)
    (masques-core/run-fn 'masques.model.peer 'init)))

(init-tests)