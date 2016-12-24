(ns async-server.handlers.study-buddy
    (:require [clojure.core.async :refer [chan go >! <! timeout]]
              [clojure.data.json :as json]
              [io.pedestal.interceptor.helpers :refer [defbefore]]
              [async-server.assistant.actions :as actions]))

;; Constants

(def ^:private conversation-api-version-header "google-assistant-api-version")
(def ^:private supported-assistant-api-versions #{"V1"})
(def ^:private max-inputs 3)

;; Helper functions

(defn- set-response-headers
  []
  {"Content-Type" "application/json"
   "Google-Assistant-API-Version" "V1"})

(defn- map-input
  "Maps an input to an action"
  [{:keys [intent arguments]}]
  (partial (get actions/action-map intent) arguments))

(defn- get-action
  "Resolves to an assistant action"
  [inputs]
  (let [actions (map map-input inputs)]
    ;; Check actions before just returning the first one
    (first actions)))

(defn- build-payload
  "Builds up the google assistant response"
  [{:keys [json-params headers] :as request}]
  (let [{user :user
         conversation :conversation
         inputs :inputs} json-params
        action (get-action inputs)]
    (action conversation user)))

;; Handler

(defbefore connect [{:keys [request response] :as context}]
  (let [ch (chan 1)]
    (go
      (let [assistant-payload (build-payload request)]
        (>! ch (assoc context :response (assoc response
                                               :status 200
                                               :headers (set-response-headers)
                                               :body (json/write-str assistant-payload))))))
    ch))
