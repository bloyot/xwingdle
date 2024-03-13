(ns xwingdle.subs
  (:require
   [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::active-panel
 (fn [db _]
   (:active-panel db)))

(def faction-symbol-map
  {:galacticempire "@"
   :firstorder "+"
   :rebelalliance "!"
   :separatistalliance "."
   :resistance "-"
   :galacticrepublic "/"
   :scumandvillainy "#"})

(def ship-symbol-map
  {   :tiewiwhispermodifiedinterceptor "#"
      :tiesffighter "S"
      :tiebainterceptor "j"
      :tiefofighter "O"
      :tievnsilencer "$"
      :xiclasslightshuttle "Q"
      :tiesebomber "!"
      :tierbheavy "J"
      :tieddefender "D"
      :tielnfighter "F"
      :gauntletfighter "6"
      :tieadvancedx1 "A"
      :tiesabomber "B"
      :tieadvancedv1 "R"
      :tieininterceptor "I"
      :tiereaper "V"
      :tieskstriker "T"
      :vt49decimator "d"
      :delta7aethersprite "\\"
      :v19torrentstarfighter "^"
      :nimbusclassvwing ","
      :eta2actis "-"
      :clonez95headhunter "}"
      :btlbywing ":"
      :arc170starfighter "C"
      :nabooroyaln1starfighter "<"
      :laatigunship "/"
      :delta7baethersprite "\\"
      :btla4ywing ":"
      :fangfighter "M"
      :vcx100lightfreighter "G"
      :z95af4headhunter "Z"
      :rz1awing "a"
      :t65xwing "x"
      :hwk290lightfreighter "h"
      :yt2400lightfreighter "o"
      :modifiedyt1300lightfreighter "m"
      :asf01bwing "b"
      :ut60duwing "u"
      :sheathipedeclassshuttle "%"
      :t70xwing "w"
      :resistancetransport ">"
      :rz2awing "E"
      :btanr2ywing "{"
      :resistancetransportpod "?"
      :fireball "0"
      :scavengedyt1300 "Y"
      :yv666lightfreighter "t"
      :m3ainterceptor "s"
      :jumpmaster5000 "p"
      :escapecraft "X"
      :rogueclassstarfighter "|"
      :customizedyt1300lightfreighter "W"
      :firesprayclasspatrolcraft "f"
      :modifiedtielnfighter "C"
      :st70assaultship "'"
      :vultureclassdroidfighter "_"
      :hmpdroidgunship "."
      :belbullab22starfighter "["
      :nantexclassstarfighter ";"
      :droidtrifighter "+"
      :hyenaclassdroidbomber "="
      :sithinfiltrator "]"})



(re-frame/reg-sub
  ::pilot-options
  (fn [db _]
      (into [] (map #(hash-map
                       :id (first %)
                       :label (:pilot-display-name (second %))
                       :faction (get faction-symbol-map (:faction (second %)))
                       :ship (get ship-symbol-map (:ship-id (second %))))
                    (:pilot-data db)))))
