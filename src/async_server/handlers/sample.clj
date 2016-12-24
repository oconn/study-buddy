(ns async-server.handlers.sample
  (:require [clojure.core.async :refer [chan go >! <! timeout]]
            [ring.util.response :as ring-resp]
            [io.pedestal.interceptor.helpers :refer [defbefore]]))

(defbefore docs [context]
  (let [ch (chan 1)]
    (go
      (>! ch (assoc context
                    :response
                    (ring-resp/response "v1 user docs"))))
    ch))

(defbefore get-users [context]
  (let [ch (chan 1)]
    (go
      (>! ch (assoc context
                    :response
                    (ring-resp/response '({:name "Matt"})))))
    ch))
