(ns xwingdle.views.components
  (:require
    [xwingdle.styles :as styles]
    [xwingdle.themes :as themes]
    [reagent-mui.styles :refer [theme-provider]]
    [reagent-mui.icons.info-outlined :refer [info-outlined]]
    [reagent-mui.icons.settings-applications :refer [settings-applications]]
    [reagent-mui.material.button :refer [button]]
    [reagent-mui.material.divider :refer [divider]]
    [reagent-mui.material.icon-button :refer [icon-button]]
    [reagent-mui.material.stack :refer [stack]]
    [reagent-mui.material.typography :refer [typography]]
    [reagent-mui.material.list-item :refer [list-item]]))

;; shared components go here
(defn header-icon-button [type]
      (case type
            :info [icon-button [info-outlined]]
            :settings [icon-button [settings-applications]]))

(defn pilot-drop-down-option [props option]
      [list-item (merge props {:key (:id option)})
       [stack {:direction "row"
               :align-items "flex-end"
               :spacing 1}
        [typography (:label option)]
        [typography {:variant "h5" :style {:font-family "xwing"}} (:faction option)]
        [typography {:variant "h5" :style {:font-family "xwing-ships"}} (:ship option)]]])


