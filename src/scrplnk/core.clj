(ns scrplnk.core
  (:require [clojure.tools.cli :refer [parse-opts]]
            [clj-http.client :as client]
            [net.cgrand.enlive-html :as html]
            [org.bovinegenius.exploding-fish :refer [absolute? resolve-uri fragment]]))

(defn link->dom [link]
  (html/html-snippet
      (:body (client/get link {:throw-exceptions false}))))

(defn dom->links [dom]
  (map (fn [x] (-> x :attrs :href)) (html/select dom [:a])))

(defn fragment? [url]
  (not (nil? (fragment url))))

(defn dom->relative [dom]
  (filter (fn [x] (and (not (fragment? x)) (not (absolute? x)))) (dom->links dom)))

(defn scrape-links [link base seen]
  (doseq [dom (link->dom link)]
    (doseq [l (filter (fn [x] (not (.contains seen (resolve-uri base x)))) (dom->relative dom))]
      (let [url (resolve-uri base l)
            seen (conj seen url)]
          (println url)
          (scrape-links url base seen)))))

(def cli-options
  [["-s" "--site" "Base url of site to scrape."]
   ["-h" "--help"]])

(defn -main [& args]
  (let [opts (parse-opts args cli-options)]
    (if (-> opts :options :site)
      (let [b (get (:arguments opts) 0)
            base b
            seen [base]]
        (scrape-links base base seen))
      (println (:summary opts)))))
