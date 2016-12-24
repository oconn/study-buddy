(ns async-server.handlers.static-pages
  (:require [clojure.data.json :as json]
            [clojure.core.async :refer [chan go >! <! timeout]]
            [ring.util.response :as ring-resp]
            [io.pedestal.interceptor.helpers :refer [defbefore]]))

(defbefore home-page [{:keys [request response] :as context}]
  (let [ch (chan 1)]
    (go
      (let [updated-response (assoc response
                                    :headers {"Content-Type" "application/json"
                                              "status" 200}
                                    :body (json/write-str {}))]
        (>! ch (assoc context
                      :response
                      (ring-resp/response "Home")))))
    ch))

(defbefore about-page [context]
  (let [ch (chan 1)]
    (go
      (>! ch (assoc context
                    :response
                    (ring-resp/response "About Page"))))
    ch))
