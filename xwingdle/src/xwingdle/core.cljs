(ns xwingdle.core
  (:require
   [reagent.dom :as rdom]
   [re-frame.core :as re-frame]
   [xwingdle.events :as events]
   [xwingdle.routes :as routes]
   [xwingdle.views.game :as game]
   [xwingdle.config :as config]))



(defn dev-setup []
  (when config/debug?
    (println "dev mode")))

(defn ^:dev/after-load mount-root []
  (re-frame/clear-subscription-cache!)
  (let [root-el (.getElementById js/document "app")]
    (rdom/unmount-component-at-node root-el)
    (rdom/render [game/main-panel] root-el)))

(defn init []
  (routes/start!)
  (re-frame/dispatch-sync [::events/initialize-db])
  (dev-setup)
  (mount-root))
