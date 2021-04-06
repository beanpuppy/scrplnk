(ns scrplnk.core
  (:require [clojure.tools.cli :refer [parse-opts]])
  (:require [clojure.string :as str]
            [clj-http.client :as client]
            [net.cgrand.enlive-html :as html]
            [org.bovinegenius.exploding-fish :refer [uri host absolute? resolve-uri fragment scheme]]))

(def base)
(def seen [])

(defn link->dom [link]
  (html/html-snippet
      (:body (client/get link {:throw-exceptions false}))))

(defn dom->links [dom]
  (map (fn [x] (-> x :attrs :href)) (html/select dom [:a])))

(defn fragment? [url]
  (not (nil? (fragment url))))

(defn dom->relative [dom]
  (filter (fn [x] (and (not (fragment? x)) (not (absolute? x)))) (dom->links dom)))

(defn scrape-links [link]
  (doseq [dom (link->dom link)]
    (doseq [l (filter (fn [x] (not (.contains seen (resolve-uri base x)))) (dom->relative dom))]
      (let [url (resolve-uri base l)]
        (def seen (conj seen url))
        (println url)
        (scrape-links url)))))

(def cli-options
  [["-s" "--site" "Base url of site to scrape."]
   ["-h" "--help"]])

(defn -main [& args]
  (let [opts (parse-opts args cli-options)]
    (if (-> opts :options :site)
      (do
        (let [b (get (:arguments opts) 0)]
          (def base b)
          (def seen (conj seen base))
          (println b)
          (scrape-links base)))
      (println (:summary opts)))))
