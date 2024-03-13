(ns xwingdle.events
  (:require
   [re-frame.core :as re-frame]
   [day8.re-frame.http-fx]
   [xwingdle.db :as db]
   [day8.re-frame.tracing :refer-macros [fn-traced]]
   [ajax.core :as ajax]
   [ajax.edn :as ajax-edn]))


(re-frame/reg-event-fx
 ::initialize-db
 (fn-traced [_ _]
            {:db db/default-db
             :fx [[:dispatch [::load-pilot-data]]]}))

(re-frame/reg-event-fx
  ::load-pilot-data
  (fn [{:keys [db]} _]
      {:http-xhrio {:method          :get
                    :uri             "/data/pilots.edn"
                    :timeout         8000                                           ;; optional see API docs
                    :response-format (ajax-edn/edn-response-format {:keywords? true})  ;; IMPORTANT!: You must provide this.
                    :on-success      [::load-pilot-data-success]
                    :on-failure      [::load-pilot-data-failure]}}))

(re-frame/reg-event-fx
  ::navigate
  (fn-traced [_ [_ handler]]
   {:navigate handler}))

(re-frame/reg-event-fx
 ::set-active-panel
 (fn-traced [{:keys [db]} [_ active-panel]]
   {:db (assoc db :active-panel active-panel)}))

;; http handler response functions
(re-frame/reg-event-db
  ::load-pilot-data-success
  (fn [db [_ result]]
      (assoc db :pilot-data result)))

(re-frame/reg-event-db
  ::load-pilot-data-failure
  (fn [db [_ result]]
      (assoc db :pilot-data result)))
