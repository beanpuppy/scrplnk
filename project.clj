(defproject scrplnk "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/tools.cli "1.0.206"]
                 [clj-http "3.12.0"]
                 [enlive "1.1.6"]
                 [org.bovinegenius/exploding-fish "0.3.6"]]
  :repl-options {:init-ns scrplnk.core}
  :main scrplnk.core)
