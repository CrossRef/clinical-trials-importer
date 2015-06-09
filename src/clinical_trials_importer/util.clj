(ns clinical-trials-importer.util)

; The regexes are scoped so tightly because there is some tricky data out there.
; This may require some maintenance but should provide the right false-negative balance.
(def prefixes {:clinical-trial-clinical-trials-gov #"nct[ -]?[-0-9]+"
               :clinical-trial-isrctn #"isrctn[ -]?[0-9-]+"
               :clinical-trial-actrn #"actrn[ -]?[0-9]+"
               :clinical-trial-drks #"drks[ -]?[0-9]+"
               :clinical-trial-chictr #"chictr[ -]?(?:-[-a-z]+)?[0-9]+"
               :clinical-trial-rebec #"rbr[ -]?[0-9-a-z]+"
               :clinical-trial-dutch-trial-register #"ntr[ -]?[-0-9]+"
               :clinical-trial-clinical-trial-registry-india #"ctri[ -]?[0-9/]+"
               :clinical-trial-umin-japan #"umin[ -]?[ctr]*[0-9]+"
               :clinical-trial-pactr #"pactr[ -]?[0-9]+"
               :clinical-trial-prospero #"crd[ -]?[0-9]+"
               :clinical-trial-slctr #"slctr[ -]?[0-9/]+"
               :clinical-trial-jma #"jma[ -]?[a-z]*[0-9]+"
               :clinical-trial-irct #"irct[ -]?[0-9a-z]+"
               :clinical-trial-hkctr #"hkctr[ -]?[-0-9]+"
               :clinical-trial-ppb-kenya #"ppb ?[0-9]+"
               :clinical-trial-ukcrn #"ukcrn ?[0-9]+"
               })


(def separator #"[-/# ]?")

; UMIN-CTR => UMIN


; (def regex (re-pattern (str "(?:" (apply str (interpose "|" (keys prefixes))) ")" separator "[0-9-/a-zA-Z]+")))
(def regex (re-pattern (str "(?:" (apply str (interpose "|" (map second prefixes))) ")")))

(defn extract-cts
  "Take a string and try and extract clinical trial IDs and types."
  [input-string]
  (let [input (.toLowerCase input-string)
        tokens (re-seq regex input)
        with-types (map (fn [token]
                          (map #(when (re-matches (second %) token)
                                  ; Some regexes can catch a space between the identifier and the number.
                                  ; The space shouldn't be there.
                                  [(.replace token " " "") (first %)])
                               prefixes)) tokens)
        ; Often duplicates.
        result (set (remove nil? (apply concat with-types)))
        
        ]
    result))
  