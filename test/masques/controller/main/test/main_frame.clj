(ns masques.controller.main.test.main-frame
  (:require [masques.test.util :as test-util])
  (:require [fixtures.profile :as profile-fixture]
            [fixtures.util :as fixtures-util]
            [masques.controller.profile.panel :as profile-panel]
            [masques.model.profile :as profile-model]
            [seesaw.core :as seesaw-core]
            test.init)
  (:use clojure.test
        masques.controller.main.main-frame))

(use-fixtures :once (join-fixtures [(fixtures-util/create-fixture [profile-fixture/fixture-map]) test-util/login-fixture]))

(deftest test-show
  (profile-model/save (assoc (profile-model/current-user) :page "Test page for the logged in user."))
  (profile-model/reload-current-user)
  (let [frame (show)]
    ;(Thread/sleep 1000)
    (is frame)
    (is (.isShowing frame))
    (show-panel frame profile-panel/panel-name-str)
    ;(Thread/sleep 10000)
    (.setVisible frame false)
    (.dispose frame)
    ;(Thread/sleep 100)
    (is (not (.isShowing frame)))
    ))