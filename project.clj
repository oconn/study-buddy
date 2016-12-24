(defproject async-server "0.0.1-SNAPSHOT"
  :description "Sample async server"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0-alpha14"]
                 [org.clojure/core.async "0.2.395"]
                 [org.clojure/data.json "0.2.6"]
                 [io.pedestal/pedestal.service "0.5.1"]
                 [io.pedestal/pedestal.jetty "0.5.1"]
                 [ch.qos.logback/logback-classic "1.1.7" :exclusions [org.slf4j/slf4j-api]]
                 [org.slf4j/jul-to-slf4j "1.7.21"]
                 [org.slf4j/jcl-over-slf4j "1.7.21"]
                 [org.slf4j/log4j-over-slf4j "1.7.21"]]
  :min-lein-version "2.0.0"
  :resource-paths ["config", "resources"]
  ;; If you use HTTP/2 or ALPN, use the java-agent to pull in the correct alpn-boot dependency
  :java-agents [[org.mortbay.jetty.alpn/jetty-alpn-agent "2.0.3"]]
  :profiles {:dev {:aliases {"run-dev" ["trampoline" "run" "-m" "async-server.server/run-dev"]}
                   :dependencies [[org.clojure/tools.namespace "0.3.0-alpha3"]
                                  [io.pedestal/pedestal.service-tools "0.5.1"]]
                   :source-paths ["dev"]}
             :uberjar {:aot [async-server.server]}}
  :main ^{:skip-aot true} async-server.server)
