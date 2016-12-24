(ns async-server.routes
  (:require [io.pedestal.http :as http]
            [io.pedestal.http.body-params :refer [body-params]]
            [async-server.handlers.static-pages :as static]
            [async-server.handlers.sample :as sample]
            [async-server.handlers.study-buddy :as study-buddy]))

(defn ok [body]
  {:status 200 :body body
   :headers {"Content-Type" "text/html"}})

(def echo
  {:name :echo
   :enter
   (fn [context]
     (let [request (:request context)
           response (ok context)]
       (assoc context :response response)))})

(def routes
  `[[["/"
      ^:interceptors [(body-params)]
      {:get static/home-page
       :post study-buddy/connect}
      ["/about" {:get static/about-page}]]
     ["/service_one/v1" {:get sample/docs}
      ^:interceptors []
      ["/users" {:get sample/get-users}]]]])
