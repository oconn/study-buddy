(ns async-server.assistant.specs
  (:require [clojure.spec :as s]))

(s/def ::headers
  (fn [headers]
    (let [version-header (get headers conversation-api-version-header)]
      (and version-header (contains? supported-assistant-api-versions version-header)))))

(s/def ::user_id string?)
(s/def ::user (s/keys :req-un [::user_id]))

(s/def ::conversation_id string?)
(s/def ::type int?)
(s/def ::conversation (s/keys :req-un [::conversation_id ::type]))

(s/def ::intent string?)
(s/def ::input (s/keys :req-un [::intent]))
(defn input?
  [value]
  (s/valid? ::input value))

(s/def ::intpus (s/coll-of input?))
(s/def ::json-params (s/keys :req-un [::user ::conversation]))

(s/def :unq/request
  (s/keys :req-un [::headers ::json-params]))

(s/conform
 :unq/request
 {:headers {"google-assistant-api-version" "V1"}
  :json-params {:user {:user_id "1231231221321"}
                :conversation {:conversation_id "12312313123"
                               :type 1}
                :inputs []}})
