(ns xwingdle-data.core
  (:require
    [camel-snake-kebab.core :as csk]
    [cheshire.core :as cheshire]
    [clojure.pprint :as pp]
    [clojure.string :as str]))

(def files
  (let [directory (clojure.java.io/file (clojure.java.io/resource "data/pilots"))
        dir? #(.isDirectory %)]
    (map #(.getPath %)
         (filter (comp not dir?)
                 (tree-seq dir? #(.listFiles %) directory)))))

(defn disambiguate-pilot-name
  "Given the pilot id and pilot name, ensure there aren't any duplicate names because
  of standard loadout or multiple releases. Append the scenario pack if applicable, or else
  use a manual override"
  [pilot-id pilot-name]
  (cond (str/includes? pilot-id "battleofyavin") (str pilot-name " (BoY)")
        (str/includes? pilot-id "siegeofcoruscant") (str pilot-name " (SoC)")
        (str/includes? pilot-id "battleoverendor") (str pilot-name " (BoE)")
        (str/includes? pilot-id "swz105") (str pilot-name " (SSP)")
        (str/includes? pilot-id "swz98") (str pilot-name " (TBE)")
        (str/includes? pilot-id "swz106") (str pilot-name " (SSP)")
        (str/includes? pilot-id "swz103") (str pilot-name " (YLF)")
        (str/includes? pilot-id "swz103-sl") (str pilot-name " (YLF-SL)")
        (str/includes? pilot-id "swz68") (str pilot-name " (HoH)")
        :else pilot-name))

(defn load-pilot
  "Given the pilots json data and an id, pull out the specific pilot/ship stats into a map"
  [ship pilot-id]
  (let [stats (:stats ship)
        pilot (first (filter #(= pilot-id (:xws %)) (:pilots ship)))
        filter-stat-fn (fn [n s] (filter #(= n (:type %)) s))
        attacks (filter-stat-fn "attack" stats)]
    {:id                 (keyword pilot-id)
     :size               (if (contains? ship :size)
                           (csk/->kebab-case-keyword (:size ship)))
     :primary_attack     (if (< 0 (count attacks)) {:arc   (csk/->kebab-case-keyword (:arc (first attacks)))
                                                    :value (:value (first attacks))})
     :secondary_attack   (if (< 1 (count attacks))
                           {:arc   (csk/->kebab-case-keyword (:arc (second attacks)))
                            :value (:value (second attacks))})
     :agility            (:value (first (filter-stat-fn "agility" stats)))
     :hull               (:value (first (filter-stat-fn "hull" stats)))
     :shields            (:value (first (filter-stat-fn "shields" stats)))
     :initiative         (:initiative pilot)
     :force              (:value (:force pilot))
     :points             (:cost pilot)
     :loadout            (:loadout pilot)
     :standard-loadout?  (contains? pilot :standardLoadout)
     :ship-display-name  (:name ship)
     :pilot-display-name (disambiguate-pilot-name pilot-id (:name pilot))
     :image              (:image pilot)
     :faction            (keyword (:faction ship))
     :ship-id            (keyword (:xws ship))
     :standard-legal?    (:standard pilot)}))


(defn load-pilots
  "For a given pilot file, return a map of the pilots with the relevant fields, keyed by xws value"
  ([pilot-file]
   (load-pilots pilot-file true))
  ([pilot-file standard-only?]
   (let [ship (cheshire/parse-string (slurp pilot-file) true)]
     (as-> (:pilots ship) p
           (map #(load-pilot ship (:xws %)) p)
           (filter #(:standard-legal? %) p)
           (group-by :id p)
           (update-vals p first)))))

(defn pretty-spit
  [file-name collection]
  (spit (java.io.File. file-name)
        (with-out-str (pp/write collection :dispatch pp/code-dispatch))))

(defn parse-pilot-data
  "convert the given xws pilot files into a single edn file with the relevant stats for
  each pilot, keyed by pilot xws value"
  []
  (pretty-spit "resources/pilots.edn" (apply merge (map load-pilots files))))