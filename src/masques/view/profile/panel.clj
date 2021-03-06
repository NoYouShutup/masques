(ns masques.view.profile.panel
  (:require [clj-internationalization.term :as term]
            [masques.view.subviews.panel :as panel-subview]
            [masques.view.utils :as view-utils]
            [seesaw.border :as seesaw-border]
            [seesaw.color :as seesaw-color]
            [seesaw.core :as seesaw-core])
  (:import [javax.swing ImageIcon]))

(def profile-name-id :profile-name)
(def profile-body-text-id :profile-body-text)
  
(defn create-header []
  (seesaw-core/border-panel
    :west (seesaw-core/flow-panel :items [(seesaw-core/text :id profile-name-id :columns 20)])
    :east (seesaw-core/vertical-panel :items [(panel-subview/create-panel-label (term/your-profile))])))

(defn create-profile-page-tools []
  (seesaw-core/vertical-panel
    :items [(seesaw-core/button :icon (ImageIcon. (ClassLoader/getSystemResource "profile.png")))
            (panel-subview/create-button :change-avatar-button (term/change-avatar))
            [:fill-v 15]
            (term/identity)
            (panel-subview/create-button :identity-text-button (term/text))
            (panel-subview/create-button :identity-qr-code-button (term/qr-code))
            [:fill-v 15]
            (term/time-zone)
            (seesaw-core/label :id :time-zone-label :text "EST (GMT -5)" :font { :name "DIALOG" :style :plain :size 10 })
            (panel-subview/create-button :time-zone-change-button (term/change))
            [:fill-v 15]
            (term/password)
            (panel-subview/create-button :password-change-button (term/change))]))
  
(defn create-profile-save-button []
  (seesaw-core/button :id :profile-save-button :text (term/save)))

(defn create-save-button-panel []
  (seesaw-core/border-panel
    :east (create-profile-save-button)))

(defn create-profile-page-body []
  (seesaw-core/scrollable
    (seesaw-core/text :id profile-body-text-id :multi-line? true :wrap-lines? true)))

(defn create-profile-page []
  (seesaw-core/border-panel
    :west (create-profile-page-tools)
    :center (create-profile-page-body)
    :south (create-save-button-panel)
    
    :hgap 5
    :border 11))

(defn create-visibility-selection-type []
  (let [selection-type-group (seesaw-core/button-group)]
    (seesaw-core/vertical-panel
      :items [(seesaw-core/radio :id :default-share-group-button 
                                 :text (term/default-share-group)
                                 :group selection-type-group
                                 :selected? true)
              [:fill-v 3]
              (seesaw-core/radio :id :all-button
                                 :text (term/all)
                                 :group selection-type-group)
              [:fill-v 3]
              (seesaw-core/radio :id :selected-button
                                 :text (term/selected)
                                 :group selection-type-group)] 

      :border (seesaw-border/line-border))))

(defn create-visibility-share-with []
  (seesaw-core/scrollable
    (seesaw-core/listbox :id :select-who-can-see-listbox)
 
    :preferred-size  [200 :by 80]))

(defn create-visibility-selection-panel []
  (seesaw-core/horizontal-panel
    :items [(create-visibility-selection-type) [:fill-h 5] 
            (create-visibility-share-with)]))

(defn create-visibility []
  (seesaw-core/border-panel
    :north (seesaw-core/border-panel
             :north (term/who-can-see-my-profile)
             :center (create-visibility-selection-panel)
             :south (seesaw-core/text 
                      :text (term/you-may-see-everyone-you-have-shared-your-profile-with)
                      :multi-line? true :wrap-lines? true :editable? false
                      :opaque? false)
    
             :vgap 5
             :border 11)))

(defn create-shares []
  (seesaw-core/border-panel
    :north (term/i-have-shared-my-profile-with)
    :center (seesaw-core/scrollable
              (seesaw-core/listbox :id :shared-profile-with-listbox))
    
    :vgap 5
    :border 11))

(defn create-advanced []
  (seesaw-core/flow-panel :items ["advanced"]))

(defn create-body []
  (seesaw-core/tabbed-panel
    :tabs [{ :title (term/profile-page) :content (create-profile-page) }
           { :title (term/visibility) :content (create-visibility) }
           { :title (term/shares) :content (create-shares) }
           { :title (term/advanced) :content (create-advanced) }]))

(defn create []
  (seesaw-core/border-panel
    :id "profile-panel"

    :north (create-header)
    :center (create-body)

    :vgap 10
    :border 11))

(defn find-profile-save-button
  "Returns the save button for the profile."
  [view]
  (view-utils/find-component view "#profile-save-button"))

(defn find-profile-name-text
  "Returns the profile name text field in the given profile panel view."
  [view]
  (view-utils/find-component view (str "#" (name profile-name-id))))
  
(defn find-profile-body-text
  "Returns the profile body text area in the given profile panel view."
  [view]
  (view-utils/find-component view (str "#" (name profile-body-text-id))))

(defn profile-body-text
  ([view]
    (seesaw-core/text (find-profile-body-text view)))
  ([view text]
    (seesaw-core/text (find-profile-body-text view) text)))
  
(defn set-profile-name-text
  "Sets the text of the profile name text field to the given text"
  [view text]
  (seesaw-core/config! (find-profile-name-text view) :text text))
  
(defn set-profile-body-text
  "Sets the text of the profile name text field to the given text"
  [view text]
  (seesaw-core/config! (find-profile-body-text view) :text text))

(defn fill-panel
  "Fills the given profile panel view with data from the given profile."
  [view profile]
  (set-profile-name-text view (:alias profile))
  (set-profile-body-text view (:page profile)))