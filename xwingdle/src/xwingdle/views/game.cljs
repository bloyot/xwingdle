(ns xwingdle.views.game
  (:require-macros [reagent-mui.util :refer [react-component]])
  (:require
    [re-frame.core :as re-frame]
    [reagent-mui.icons.help-outlined :refer [help-outlined]]
    [reagent-mui.icons.info-outlined :refer [info-outlined]]
    [reagent-mui.material.button :refer [button]]
    [reagent-mui.material.card :refer [card]]
    [reagent-mui.material.container :refer [container]]
    [reagent-mui.material.divider :refer [divider]]
    [reagent-mui.material.icon-button :refer [icon-button]]
    [reagent-mui.material.stack :refer [stack]]
    [reagent-mui.material.typography :refer [typography]]
    [reagent-mui.material.autocomplete :refer [autocomplete]]
    [reagent-mui.material.text-field :refer [text-field]]
    [reagent-mui.material.css-baseline :refer [css-baseline]]
    [reagent-mui.styles :refer [theme-provider]]
    [xwingdle.routes :as routes]
    [xwingdle.subs :as subs]
    [xwingdle.views.components :as components]
    [xwingdle.themes :as themes]))


(defn header []
      [stack
       {:direction       "row"
        :justify-content "center"
        :align-items     "center"
        :spacing         2}
       [typography {:variant "h2"} "xwinglde"]
       (components/header-icon-button :info)
       (components/header-icon-button :settings)])
(defn cards []
      (let [pilots (re-frame/subscribe [::subs/pilot-options])]
           [stack
            {:direction       "row"
             :justify-content "space-around"
             :align-items     "center"
             :spacing         2}
            [autocomplete
             {:options       @pilots
              :sx            {:width 300}
              :render-input  (react-component [props]
                                              [text-field (merge props
                                                                 {:label "Guess a pilot card"})])
              :render-option (react-component
                               [props option]
                               (components/pilot-drop-down-option props option))}]

            [button "View Cards"]]))



(defn grid [])

(defn game-panel []
      [theme-provider themes/default-theme
       [css-baseline
        [container
         [stack
          {:spacing 2}
          (header)
          [divider {:variant "middle"}]
          (cards)]]]])




(defmethod routes/panels :game-panel [] [game-panel])

;; main
(defn main-panel []
      (let [active-panel (re-frame/subscribe [::subs/active-panel])]
           (routes/panels @active-panel)))

