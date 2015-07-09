; Regular expressions:
; All CTNs will be lower-cased before processing.
; :regular-expression-relaxed is used to find CTNs in free text. They often have extra dashes etc.
; :regular-expression-strict is used to validate a correct CTN. Often the first dash shouldn't be there.
; :regular-expression-cleanup is a find-replace pair to attempt to fix the found CTN to make it valid.
; :test is a sequence of pairs of inputs to outputs to test the process involving the above three.

[ 
    {:id "clinical-trial:utn"
     :type :clinical-trial-registry
     :name "Uniform Trial Number"
     :short-name "UTN"
     :regular-expression-relaxed "utn[ -]?\\d+"
     :regular-expression-strict "utn\\d+"
     :regular-expression-cleanup ["utn[ -](\\d+)", "utn$1"]
     :test [["utn1234", "utn1234"], ["utn-1234", "utn1234"], ["utn 1234", "utn1234"]]
     :create-url "http://apps.who.int/trialsearch/Trial2.aspx?TrialID=%s"
     :url "http://www.who.int/ictrp/unambiguous_identification/en/"
     :note "UTN is not a trial registry"}
    
    {:id "clinical-trial:clinical-trials-gov"
     :type :clinical-trial-registry
     :name "ClinicalTrials.gov"
     :short-name "ClinicalTrials.gov"
     :regular-expression-relaxed "nct[ -]?\\d+"
     :regular-expression-strict "nct\\d+"
     :regular-expression-cleanup ["nct[ -](\\d+)", "nct$1"]
     :test [["nct1234", "nct1234"], ["nct 1234", "nct1234"], ["nct-1234", "nct1234"]]
     :create-url "https://clinicaltrials.gov/ct2/show/%s"
     :url "http://www.clinicaltrials.gov/"}

    {:id "clinical-trial:isrctn"
     :type :clinical-trial-registry
     :name "ISRCTN.org"
     :short-name "ISRCTN"
     :regular-expression-relaxed "isrctn[ -]?\\d+"
     :regular-expression-strict "isrctn\\d+"
     :regular-expression-cleanup ["isrctn[ -](\\d+)", "isrctn$1"]
     :test [["isrctn1234", "isrctn1234"], ["isrctn 1234", "isrctn1234"], ["isrctn-1234", "isrctn1234"]]
     :create-url "http://www.isrctn.com/%s"
     :url "http://www.isrctn.com/"
     :notes "Previously controlled-trials.com"}

    {:id "clinical-trial:anzctr"
     :type :clinical-trial-registry
     :name "Australian New Zealand Clinical Trials Registry"
     :short-name "ANZCTR"
     :regular-expression-relaxed "actrn[ -]?\\d+"
     :regular-expression-strict "actrn\\d+"
     :regular-expression-cleanup ["actrn[ -](\\d+)", "actrn$1"]
     :test [["actrn1234", "actrn1234"], ["actrn 1234", "actrn1234"], ["actrn-1234", "actrn1234"]]
     :create-url "http://www.anzctr.org.au/AdvanceSearch.aspx?searchTxt=%s"
     :url "http://www.anzctr.org.au/"
     :notes "changed name from ANCTR to ANZCTR but identifier prefixes remain the same."}
    
    {:id "clinical-trial:drks"
     :type :clinical-trial-registry
     :name "German Clinical Trials Register"
     :short-name "DRKS"
     :regular-expression-relaxed "drks[ -]?\\d+"
     :regular-expression-strict "drks\\d+"
     :regular-expression-cleanup ["drks[ -](\\d+)", "drks$1"]
     :test [["drks1234", "drks1234"], ["drks 1234", "drks1234"], ["drks-1234", "drks1234"]]
     :create-url "https://drks-neu.uniklinik-freiburg.de/drks_web/navigate.do?navigationId=trial.HTML&TRIAL_ID=%s"
     :url "https://drks-neu.uniklinik-freiburg.de/"}

    {:id "clinical-trial:chictr"
     :type :clinical-trial-registry
     :name "Chinese Clinical Trial Registry"
     :short-name "ChiCTR"
     :example "ChiCTR-TRC-14005244"
     :regular-expression-relaxed "chictr[ -]?[-a-z]+-\\d+"
     :regular-expression-strict "chictr-[-a-z]+-\\d+"
     ; If there's a space remove it. If there's a hyphen, it's meant to be there.
     :regular-expression-cleanup ["chictr[ -]?([a-z][-a-z]+)", "chictr-$1"]
     :test [["ChiCTR-TRC-14005244", "chictr-trc-14005244"], ["ChiCTR TRC-14005244", "chictr-trc-14005244"]]
     :create-url "http://www.chictr.org/en/proj/search.aspx?regno=%s"
     :url "http://www.chictr.org/"}

    {:id "clinical-trial:rebec"
     :type :clinical-trial-registry
     :name "Brazilian Clinical Trials Registry / Registro Brasileiro de Ensaios Clínicos "
     :short-name "ReBec"
     :regular-expression-relaxed "rbr[ -]?\\w+"
     :regular-expression-strict "rbr\\w+"
     :regular-expression-cleanup ["rbr[ -](\\w+)", "rbr$1"]
     :test [["rbr1234", "rbr1234"], ["rbr 1234", "rbr1234"], ["rbr-1234", "rbr1234"]]
     :create-url "http://www.ensaiosclinicos.gov.br/rg/%s"
     :url "http://www.ensaiosclinicos.gov.br/"}
    
    {:id "clinical-trial:dutch-trial-register"
     :type :clinical-trial-registry
     :name "Netherlands National Trial Register"
     :short-name "NTR"
     :regular-expression-relaxed "ntr[ -]?[-0-9]+"
     :regular-expression-strict "ntr[-0-9]+"
     :regular-expression-cleanup ["ntr[ -](\\d+)", "ntr$1"]
     :test [["ntr1234", "ntr1234"], ["ntr 1234", "ntr1234"], ["ntr-1234", "ntr1234"]]
     :create-url "http://www.trialregister.nl/trialreg/admin/rctview.asp?TC"
     :url "http://www.trialregister.nl"}

    {:id "clinical-trial:clinical-trial-registry-india"
     :type :clinical-trial-registry
     :name "Clinical Trials Registry India"
     :short-name "CTRI"
     :regular-expression-relaxed "ctri[ -]?[\\d/]+"
     :regular-expression-strict "ctri[\\d/]+"
     :regular-expression-cleanup ["ctri[ -]([\\d/]+)", "ctri$1"]
     :test [["ctri1234", "ctri1234"], ["ctri 1234", "ctri1234"], ["ctri-1234", "ctri1234"]]
     ; The CTRI website prevents attempts to search or link directly to trials.
     :create-url nil     
     :url "http://www.ctri.nic.in/Clinicaltrials"}
    
    {:id "clinical-trial:umin-japan"
     :type :clinical-trial-registry
     :name "UMIN Japan"
     :short-name "UMIN"
     ; Note on prefix https://upload.umin.ac.jp/ctr/announce/ctrno_prefix_e.htm
     :regular-expression-relaxed "umin[ -]?\\d+"
     :regular-expression-strict "umin\\d+"
     :regular-expression-cleanup ["umin[ -](\\d+)", "umin$1"]
     :test [["umin1234", "umin1234"], ["umin 1234", "umin1234"], ["umin-1234", "umin1234"]]
     :create-url nil
     :url "http://www.umin.ac.jp/ctr/"}

    {:id "clinical-trial:pactr"
     :type :clinical-trial-registry
     :name "Pan African Clinical Trial Registry"
     :short-name "PACTR"
     :regular-expression-relaxed "pactr[ -]?\\d+"    
     :regular-expression-strict "pactr\\d+"
     :regular-expression-cleanup ["pactr[ -](\\d+)", "pactr$1"]
     :test [["pactr1234", "pactr1234"], ["pactr 1234", "pactr1234"], ["pactr-1234", "pactr1234"]]
     ; PACTR resists linking, right-click, copy, paste...
     :create-url nil
     :url "http://www.pactr.org/"}
    
    {:id "clinical-trial:prospero"
     :type :clinical-trial-registry
     :name "International Prospective Register of Systematic Reviews"
     :short-name "Prospero"
     :regular-expression-relaxed "crd[ -]?\\d+"
     :regular-expression-strict "crd\\d+"
     :regular-expression-cleanup ["crd[ -](\\d+)", "crd$1"]
     :test [["crd1234", "crd1234"], ["crd-1234", "crd1234"], ["crd 1234", "crd1234"]]
     :create-url "http://www.crd.york.ac.uk/prospero/display_record.asp?ID=%s"
     :url "http://www.crd.york.ac.uk/prospero"}
    
    {:id "clinical-trial:slctr"
     :type :clinical-trial-registry
     :name "Sri Lanka Clinical Trials Registry"
     :short-name "SLCTR"
     :regular-expression-relaxed "slctr[ -]?[\\d/]+"
     :regular-expression-strict "slctr[\\d/]+"
     :regular-expression-cleanup ["slctr[ -]([\\d/]+)", "slctr$1"]
     :test [["slctr1234", "slctr1234"], ["slctr-1234", "slctr1234"], ["slctr 1234", "slctr1234"]]
     ; SLCTR search doesn't work with identifier.
     :create-url nil
     :url "http://slctr.lk/"}

    {:id "clinical-trial:irct"
     :type :clinical-trial-registry
     :name "Iranian Registry of Clinical Trials"
     :short-name "IRCT"
     :regular-expression-relaxed "irct[ -]?[0-9a-z]+"   
     :regular-expression-strict "irct[0-9a-z]+"
     :regular-expression-cleanup ["irct[ -]([0-9a-z]+)", "irct$1"]
     :test [["IRCT138705301058N1", "irct138705301058n1"], ["IRCT 138705301058n1", "irct138705301058n1"], ["IRCT-138705301058N1", "irct138705301058n1"]]
     :create-url "http://www.irct.ir/searchen.php?&field=a&lang=en&keyword="
     :url "http://irct.ir/"}

    {:id "clinical-trial:hkctr"
     :type :clinical-trial-registry
     :name "Hong Kong Clinical Trial Register"
     :short-name "HKCTR"
     :regular-expression-relaxed "hkctr[- ]?\\d+"
     :regular-expression-strict "hkctr-\\d+"
     ; The dash is required. Vaccum up and put back.
     :regular-expression-cleanup ["hkctr[ -]*(\\d+)", "hkctr-$1"]
     :test [["hkctr-1234", "hkctr-1234"], ["hkctr 1234", "hkctr-1234"], ["hkctr1234", "hkctr-1234"]]
     :url "http://www.hkclinicaltrials.com/"}

    {:id "clinical-trial:ppb-kenya"
     :type :clinical-trial-registry
     :regular-expression-relaxed "ppb[- ]?\\d+"
     :regular-expression-strict "ppb\\d+"
     :regular-expression-cleanup ["ppb[- ]?(\\d+)", "ppb$1"]
     :test [["ppb1234", "ppb1234"], ["ppb 1234", "ppb1234"], ["ppb-1234", "ppb1234"]]
     :name "Pharmacy and Poisons Board, Kenya"
     :url "http://www.ctr.pharmacyboardkenya.org/"}

    {:id "clinical-trial:ukcrn"
     :type :clinical-trial-registry
     :regular-expression-relaxed "ukcrn[- ]?\\d+"
     :regular-expression-strict "ukcrn\\d+"
     :regular-expression-cleanup ["ukcrn[- ]?(\\d+)", "ukcrn$1"]
     :test [["ukcrn1234", "ukcrn1234"], ["ukcrn 1234", "ukcrn1234"], ["ukcrn-1234", "ukcrn1234"]]
     :name "UK Clinical Research Network Study Portfolio"
     ; if a study is not found it will still return 200 OK
     :create-url "http://public.ukcrn.org.uk/search/StudyDetail.aspx?StudyID=%n"
     :url "http://public.ukcrn.org.uk/search/"}

    {:id "clinical-trial:cris"
     :type :clinical-trial-registry
     :name "Clinical Research Information Service, Republic of Korea"    
     :short-name "CRiS"
     :regular-expression-relaxed "kct[ -]?\\d+"
     :regular-expression-strict "kct\\d+"
     :regular-expression-cleanup ["kct[- ]?(\\d+)", "kct$1"]
     :test [["kct1234", "kct1234"], ["kct 1234", "kct1234"], ["kct-1234", "kct1234"]]
     :create-url "https://cris.nih.go.kr/cris/search/basic_search.jsp?searchword="
     :url "https://cris.nih.go.kr"}
    
    {:id "clinical-trial:rpec "
     :type :clinical-trial-registry
     :name "Cuban Public Registry of Clinical Trials"
     :short-name "RPCEC"
     :example "RPCEC00000192"
     :regular-expression-relaxed "rpec[ -]?\\d+"
     :regular-expression-strict "rpec\\d+"
     :regular-expression-cleanup ["rpec[- ]?(\\d+)", "rpec$1"]
     :test [["rpec1234", "rpec1234"], ["rpec 1234", "rpec1234"], ["rpec-1234", "rpec1234"]]
     :create-url "http://registroclinico.sld.cu/ensayos/"
     :url "http://registroclinico.sld.cu"}
    
    {:id "clinical-trial:euctr"
     :type :clinical-trial-registry
     :name "EU Clinical Trials Register"
     :short-name "EU-CTR"
     :regular-expression-relaxed "\\d{4}-\\d+-\\d+"
     :regular-expression-strict "\\d{4}-\\d+-\\d+"
     :test [["2004-001383-46", "2004-001383-46"]]
     :create-url "https://www.clinicaltrialsregister.eu/ctr-search/search?query="
     :url "https://www.clinicaltrialsregister.eu"}
    
    {:id "clinical-trial:tctr"
     :type :clinical-trial-registry
     :name "Thai Clinical Trials Registry"
     :short-name "TCTR"
     :regular-expression-relaxed "tctr[ -]?\\d+" 
     :regular-expression-strict "tctr\\d+"
     :regular-expression-cleanup ["tctr[- ]?(\\d+)", "tctr$1"]
     :test [["tctr1234", "tctr1234"], ["tctr 1234", "tctr1234"], ["tctr-1234", "tctr1234"]]
     ; TCTR resists linking, right-click, copy, paste...
     :create-url "http://www.clinicaltrials.in.th/"
     :url nil}]