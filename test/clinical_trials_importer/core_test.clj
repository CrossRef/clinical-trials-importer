(ns clinical-trials-importer.core-test
  (:require [clojure.test :refer :all]
            [clinical-trials-importer.util :as util]))


; scroll down....    
    
; Inputs taken from the PLOS api, these are marked as 'clinical trial IDs'.
(def plos-inputs
  [

  ; Not sure what this is.
  ["#TR-CCH-Chi CTR-CCH-00000361" []] 

  ["1) Study 1: ClinicalTrials.gov NCT01745458 2) Study 2: ClinicalTrials.gov NCT00387257" [["nct01745458" :clinical-trial-clinical-trials-gov] ["nct00387257" :clinical-trial-clinical-trials-gov]]]

  [": ACTR.org.au ACTRN12608000628347" [["actrn12608000628347" :clinical-trial-actrn]]]

  ["ACTR.org.au ACTRN012605000781640" [["actrn012605000781640" :clinical-trial-actrn]]]

  ["Aerobic training, aerobic-resistance training and glucose profile (CGMS) in type 2 diabetes (CGMS exercise). ClinicalTrials.gov ID: NCT00887094." [["nct00887094" :clinical-trial-clinical-trials-gov]]]

  ["Aging is associated ....  ClinicalTrials.gov NCT00473902" [["nct00473902" :clinical-trial-clinical-trials-gov]]]

  ; Genbank isn't a clinical trial registry.
  ["All sequences submitted to Genbank. Accession number: JX914505 to JX914564." []]

  ["ANZCTR.org.au ACTRN12610000509066" [["actrn12610000509066" :clinical-trial-actrn]]]

  ["Australia & New Zealand Clinical Trial Registry ACTRN12612000115831 www.anzctr.org.au" [["actrn12612000115831" :clinical-trial-actrn]]]

  ["Australia and New Zealand Clinical Trials Registry ACTRN12611001187932" [["actrn12611001187932" :clinical-trial-actrn]]]

  ; This was a typo in 'actrn'.
  ["Australia New Zealand Clinical Trial Registry ANZRN12612000027819 https://www.anzctr.org.au/Trial/Registration/TrialReview.aspx?id=347799." []]

  ; No prefix, can't really guess without more effort.
  ["Australia New Zealand Clinical Trials Registry 12610000944033" []]

  ["Australian and New Zealand Clinical Trials Registry ACTRN12610000085077" [["actrn12610000085077" :clinical-trial-actrn]]]

  ["Australian New Zealand Clinical Trial Registry (ANZCTR) ACTRN1260500032065" [["actrn1260500032065" :clinical-trial-actrn]]]

  ; No prefix
  ["Australian New Zealand Clinical Trials Registry #336317." []]

  ["Australian New Zealand Clinical Trials Registry (ANZCTR) ACTRN12614000942651" [["actrn12614000942651" :clinical-trial-actrn]]]

  ; Typo in identifier
  ["Australian New Zealand Clinical Trials Registry ACTN12612000743864." []]

  ["Australian New Zealand Clinical Trials Registry ACTRN012606000098538" [["actrn012606000098538" :clinical-trial-actrn]]]

  ; Space between identifier and number.
  ["Australian New Zealand Clinical Trials Registry ANZCTRN 12608000459325" []]

  ["Australian New Zealand Clinical Trials, ACTRN12614000817640, http://www.anzctr.org.au/." [["actrn12614000817640" :clinical-trial-actrn]]]

  ["Brazilian Clinical Trials Registry (ReBec) - RBR-6gdyvz - http://www.ensaiosclinicos.gov.br/rg/?q=RBR-6gdyvz" [["rbr-6gdyvz" :clinical-trial-rebec] ["rbr-6gdyvz" :clinical-trial-rebec]]]

  ["ChiCTR ChiCTR-OOR-14005551" [["chictr-oor-14005551" :clinical-trial-chictr]]]

  ["ChiCTR.org ChiCTR-OCS-11001173" [["chictr-ocs-11001173" :clinical-trial-chictr]]]

  ["ChiCTR.org ChiCTR-TTRCC-13003835" [["chictr-ttrcc-13003835" :clinical-trial-chictr]]]

  ["Chinese Clinical Trial Register center ChiCTR-TRC-09000530" [["chictr-trc-09000530" :clinical-trial-chictr]]]

  ["Chinese State Food and Drug Administration (SFDA) 2002SL0046; Controlled-Trials.com ISRCTN66850051" [["isrctn66850051" :clinical-trial-isrctn]]]

  ["ClinialTrials.gov NCT00287300" [["nct00287300" :clinical-trial-clinical-trials-gov]]]

  ; KCT appears to be a prefix from Kissei Pharmaceutical, but doesn't appear to bea  proper registry.
  ; https://clinicaltrials.gov/ct2/show/NCT01211951
  ["Clinical Research Information Service (CRIS) KCT0000048" []]

  ["Clinical Trail Registration India (CTRI) CTRI/2012/08/002884" [["ctri/2012/08/002884" :clinical-trial-clinical-trial-registry-india]]]

  ["Clinical Trial Registration: NCT01391494 and NCT01512706." [["nct01391494" :clinical-trial-clinical-trials-gov] ["nct01512706" :clinical-trial-clinical-trials-gov]]]

  ["Clinical Trial Registry CTRI/2009/091/000051" [["ctri/2009/091/000051" :clinical-trial-clinical-trial-registry-india]]]

  ["Clinical Trial Registry – India CTRI/2010/091/000114" [["ctri/2010/091/000114" :clinical-trial-clinical-trial-registry-india]]]

  ["Clinical Trial Registry, India CTRI/2010/091/000301" [["ctri/2010/091/000301" :clinical-trial-clinical-trial-registry-india]]]

  ["Clinical trial.gov NCT00235287" [["nct00235287" :clinical-trial-clinical-trials-gov]]]

  ["Clinical Trials Registry of India CTRI/2012/10/003060" [["ctri/2012/10/003060" :clinical-trial-clinical-trial-registry-india]]]

  ["Clinical Trials Registry-India, National Institute of Medical Statistics (Indian Council of Medical Research) CTRI/2012/04/002582" [["ctri/2012/04/002582" :clinical-trial-clinical-trial-registry-india]]]

  ["Clinical Trials. gov NCT01514708" [["nct01514708" :clinical-trial-clinical-trials-gov]]]

  ["Clinical Trials.gov NCT00036764" [["nct00036764" :clinical-trial-clinical-trials-gov]]]

  ["Clinical-Trials.gov: NCT01117675." [["nct01117675" :clinical-trial-clinical-trials-gov]]]

  ["ClinicalTrails.gov NCT00102089, NCT00108654" [["nct00102089" :clinical-trial-clinical-trials-gov] ["nct00108654" :clinical-trial-clinical-trials-gov]]]

  ["ClinicalTrails.gov NCT00279110 NCT00279110&quest;term&hairsp;&equals;&hairsp;NCT00279110&amp;rank&hairsp;&equals;&hairsp;1" [["nct00279110" :clinical-trial-clinical-trials-gov] ["nct00279110" :clinical-trial-clinical-trials-gov]]]

  ["ClinicalTrails.gov NCT00300768" [["nct00300768" :clinical-trial-clinical-trials-gov]]]

  ["ClinicalTrails.gov NCT00505050 (REMADHE)" [["nct00505050" :clinical-trial-clinical-trials-gov]]]

  ["ClinicalTrails.gov NCT01920750 (Phase I), NCT00128193 (Phase II)" [["nct01920750" :clinical-trial-clinical-trials-gov] ["nct00128193" :clinical-trial-clinical-trials-gov]]]

  ["Clinicaltrial.gov identifier NCT00736190" [["nct00736190" :clinical-trial-clinical-trials-gov]]]

  ["ClinicalTrial.gov NCT00248469." [["nct00248469" :clinical-trial-clinical-trials-gov]]]

  ["Clinicaltrial.gov NCT1224028" [["nct1224028" :clinical-trial-clinical-trials-gov]]]

  ["ClinicalTrials gov NCT00273780" [["nct00273780" :clinical-trial-clinical-trials-gov]]]

  ["ClinicalTrials. gov NCT00421551 <NCT00421551>" [["nct00421551" :clinical-trial-clinical-trials-gov] ["nct00421551" :clinical-trial-clinical-trials-gov]]]

  ["Clinicaltrials.gov (ID NCT00828152)" [["nct00828152" :clinical-trial-clinical-trials-gov]]]

  ["ClinicalTrials.gov (Identifier NCT01187069)." [["nct01187069" :clinical-trial-clinical-trials-gov]]]

  ["ClinicalTrials.gov +++++NCT01128790" [["nct01128790" :clinical-trial-clinical-trials-gov]]]

  ["ClinicalTrials.gov +NCT00903084." [["nct00903084" :clinical-trial-clinical-trials-gov]]]

  ["ClinicalTrials.gov +NCT00928798" [["nct00928798" :clinical-trial-clinical-trials-gov]]]

  ["ClinicalTrials.gov [NCT00279812]" [["nct00279812" :clinical-trial-clinical-trials-gov]]]

  ["ClinicalTrials.gov [NCT00386841" [["nct00386841" :clinical-trial-clinical-trials-gov]]]

  ["ClinicalTrials.gov [NCT00984763]" [["nct00984763" :clinical-trial-clinical-trials-gov]]]

  ["ClinicalTrials.gov __NCT01268787" [["nct01268787" :clinical-trial-clinical-trials-gov]]]

  ["ClinicalTrials.gov as NCT00281229" [["nct00281229" :clinical-trial-clinical-trials-gov]]]

  ; This happened.
  ["ClinicalTrials.gov bold>" []]

  ["ClinicalTrials.gov ChiCTR-TRC-11001359" [["chictr-trc-11001359" :clinical-trial-chictr]]]

  ; Looks like this is a typo, should be "NCT-2005-015374"
  ["ClinicalTrials.gov CT-2005-015374" []]

  ["ClinicalTrials.gov ID: NCT01616030" [["nct01616030" :clinical-trial-clinical-trials-gov]]]

  ["Clinicaltrials.gov Identifier NCT00600548" [["nct00600548" :clinical-trial-clinical-trials-gov]]]

  ["ClinicalTrials.gov Identifier: NCT01875146" [["nct01875146" :clinical-trial-clinical-trials-gov]]]

  ["ClinicalTrials.gov Identifiers: FLU 002- NCT01056354, FLU 003- NCT01056185." [["nct01056354" :clinical-trial-clinical-trials-gov] ["nct01056185" :clinical-trial-clinical-trials-gov]]]

  ["ClinicalTrials.gov IRCT201202199014N1" [["irct201202199014n1" :clinical-trial-irct]]]

  ["ClinicalTrials.gov ISRCTN51695599" [["isrctn51695599" :clinical-trial-isrctn]]]

  ; Space in the identifier should be found and removed.
  ["ClinicalTrials.gov NCT 00790894" [["nct00790894" :clinical-trial-clinical-trials-gov]]]

  ; Space in the identifier should be found and removed.
  ["ClinicalTrials.gov NCT 00811421; Pan African Clinical Trials Registry PACTR 2010020001813440" [["nct00811421" :clinical-trial-clinical-trials-gov] ["pactr2010020001813440" :clinical-trial-pactr]]]

  ; Extraneous '#' character.
  ["Clinicaltrials.gov NCT#01059071" []]

  ; Dash should be found and removed as we can't be certain.
  ["ClinicalTrials.gov NCT-01165463" [["nct-01165463" :clinical-trial-clinical-trials-gov]]]

  ["ClinicalTrials.gov NCT00007488" [["nct00007488" :clinical-trial-clinical-trials-gov]]]

  ["ClinicalTrials.gov NCT00011648 http://clinicaltrials.gov/" [["nct00011648" :clinical-trial-clinical-trials-gov]]]

  ["ClinicalTrials.gov NCT00015704" [["nct00015704" :clinical-trial-clinical-trials-gov]]]

  ["ClinicalTrials.gov NCT00027352; NCT00004978" [["nct00027352" :clinical-trial-clinical-trials-gov] ["nct00004978" :clinical-trial-clinical-trials-gov]]]

  ["ClinicalTrials.gov NCT00038974" [["nct00038974" :clinical-trial-clinical-trials-gov]]]

  ["ClinicalTrials.gov NCT00082446 NCT003766090 NCT00252148 NCT00083603 NCT00301184 NCT00428337" [["nct00082446" :clinical-trial-clinical-trials-gov]
                                                                                                   ["nct003766090" :clinical-trial-clinical-trials-gov]
                                                                                                   ["nct00252148" :clinical-trial-clinical-trials-gov]
                                                                                                   ["nct00083603" :clinical-trial-clinical-trials-gov]
                                                                                                   ["nct00301184" :clinical-trial-clinical-trials-gov]
                                                                                                   ["nct00428337" :clinical-trial-clinical-trials-gov]]]

  ["ClinicalTrials.gov NCT00153777 and Current Controlled Trials ISRCTN95638385" [["nct00153777" :clinical-trial-clinical-trials-gov] ["isrctn95638385" :clinical-trial-isrctn]]]

  ["ClinicalTrials.gov NCT00211224. [NCT00211224]" [["nct00211224" :clinical-trial-clinical-trials-gov]]]

  ["ClinicalTrials.gov NCT00215800" [["nct00215800" :clinical-trial-clinical-trials-gov]]]

  ; Tricky!
  ["ClinicalTrials.gov NCT00219401NCT00219401" [["nct00219401" :clinical-trial-clinical-trials-gov]
                                                ["nct00219401" :clinical-trial-clinical-trials-gov]]]

  ["ClinicalTrials.gov NCT00221364" [["nct00221364" :clinical-trial-clinical-trials-gov]]]

  ["ClinicalTrials.gov NCT00271752 http://clinicaltrials.gov/ct2/show/NCT00271752" [["nct00271752" :clinical-trial-clinical-trials-gov] ["nct00271752" :clinical-trial-clinical-trials-gov]]]

  ["ClinicalTrials.gov NCT00317993 NCT00317993." [["nct00317993" :clinical-trial-clinical-trials-gov] ["nct00317993" :clinical-trial-clinical-trials-gov]]]

  ["ClinicalTrials.gov NCT00317993" [["nct00317993" :clinical-trial-clinical-trials-gov]]]

  ["ClinicalTrials.gov NCT00336947 http://clinicaltrials.gov/show/NCT00336947" [["nct00336947" :clinical-trial-clinical-trials-gov] ["nct00336947" :clinical-trial-clinical-trials-gov]]]

  ["ClinicalTrials.gov NCT00349622." [["nct00349622" :clinical-trial-clinical-trials-gov]]]

  ["ClinicalTrials.gov NCT00358332 [NCT00358332]" [["nct00358332" :clinical-trial-clinical-trials-gov]]]

  ["ClinicalTrials.gov NCT00390962 (Retrospective analysis of this cohort)." [["nct00390962" :clinical-trial-clinical-trials-gov]]]

  ["ClinicalTrials.gov NCT00393679; Pan African Clinical Trials Registry PACTR2009010000911750" [["nct00393679" :clinical-trial-clinical-trials-gov] ["pactr2009010000911750" :clinical-trial-pactr]]]

  ["ClinicalTrials.gov NCT00427453 (short boosting interval), NCT00427830 (long boosting interval), NCT00480714 (BCG alone)" [["nct00427453" :clinical-trial-clinical-trials-gov] ["nct00427830" :clinical-trial-clinical-trials-gov] ["nct00480714" :clinical-trial-clinical-trials-gov]]]

  ["ClinicalTrials.gov NCT00432081" [["nct00432081" :clinical-trial-clinical-trials-gov]]]

  ["ClinicalTrials.gov NCT00432367 [NCT00432367]" [["nct00432367" :clinical-trial-clinical-trials-gov]]]

  ["Clinicaltrials.gov NCT00436072." [["nct00436072" :clinical-trial-clinical-trials-gov]]]

  ["ClinicalTrials.gov NCT00460317, NCT00369070" [["nct00460317" :clinical-trial-clinical-trials-gov] ["nct00369070" :clinical-trial-clinical-trials-gov]]]

  ["Clinicaltrials.gov NCT00460525 NCT00460525" [["nct00460525" :clinical-trial-clinical-trials-gov] ["nct00460525" :clinical-trial-clinical-trials-gov]]]

  ["ClinicalTrials.gov NCT00504075 Clinical Trials Registry India 000016" [["nct00504075" :clinical-trial-clinical-trials-gov]]]

  ["Clinicaltrials.gov NCT00511420 and NCT00502047" [["nct00511420" :clinical-trial-clinical-trials-gov] ["nct00502047" :clinical-trial-clinical-trials-gov]]]

  ["ClinicalTrials.gov NCT0052195" [["nct0052195" :clinical-trial-clinical-trials-gov]]]

  ["ClinicalTrials.gov NCT00522860 and NCT00522912" [["nct00522860" :clinical-trial-clinical-trials-gov] ["nct00522912" :clinical-trial-clinical-trials-gov]]]

  ["ClinicalTrials.gov NCT00529828" [["nct00529828" :clinical-trial-clinical-trials-gov]]]

  ["clinicaltrials.gov NCT00530439, NCT01918319 and NCT01918423. URL: NCT00530439&quest;term&hairsp;&equals;&hairsp;NCT00530439&amp;rank&hairsp;&equals;&hairsp;1, NCT01918319&quest;term&hairsp;&equals;&hairsp;NCT00530439&amp;rank&hairsp;&equals;&hairsp;2 and NCT01918423&quest;term&hairsp;&equals;&hairsp;NCT00530439&amp;rank&hairsp;&equals;&hairsp;3." [["nct00530439" :clinical-trial-clinical-trials-gov] ["nct01918319" :clinical-trial-clinical-trials-gov] ["nct01918423" :clinical-trial-clinical-trials-gov] ["nct00530439" :clinical-trial-clinical-trials-gov] ["nct01918319" :clinical-trial-clinical-trials-gov] ["nct01918423" :clinical-trial-clinical-trials-gov]]]

  ["Current Controlled Trails NTR1166." [["ntr1166" :clinical-trial-dutch-trial-register]]]

  ["Current Controlled Trials ISRCTN 42944026 www.controlled-trials.com/ISRCTN42944026" [["isrctn42944026" :clinical-trial-isrctn]]]

  
  ["Current Controlled Trials ISRCTN 48153354" [["isrctn48153354" :clinical-trial-isrctn]]]

  ["Current Controlled Trials ISRCTN59275137 and Kenya Pharmacy and Poisons Board Ethical Committee for Clinical Trials PPB/ECCT/08/07." [["isrctn59275137" :clinical-trial-isrctn]]]

  ["Current Controlled Trials ISRCTN59497461" [["isrctn59497461" :clinical-trial-isrctn]]]

  ["Current Controlled Trials ISRCTN71423189 (Study A)." [["isrctn71423189" :clinical-trial-isrctn]]]

  ["Current Controlled Trials ISRCTN92551397 http://www.controlled-trials.com/ISRCTN92551397" [["isrctn92551397" :clinical-trial-isrctn] ["isrctn92551397" :clinical-trial-isrctn]]]

  ["Current Controlled Trials ISRCTN96256793" [["isrctn96256793" :clinical-trial-isrctn]]]

  ["Current Controlled Trials Ltd. c/o BioMed Central ISRCTN68125331" [["isrctn68125331" :clinical-trial-isrctn]]]

  ["Current Controlled Trials NCT00286377" [["nct00286377" :clinical-trial-clinical-trials-gov]]]

  ["Current Controlled Trials NCT00699920" [["nct00699920" :clinical-trial-clinical-trials-gov]]]

  ["Current Controlled Trials number ISRCTN38002730." [["isrctn38002730" :clinical-trial-isrctn]]]

  ["Current Controlled Trials, ISRCTN39642997 (http://www.controlled-trials.com/ISRCTN39642997)" [["isrctn39642997" :clinical-trial-isrctn] ["isrctn39642997" :clinical-trial-isrctn]]]

  ["Current Controlled Trials. ISRCTN64716212." [["isrctn64716212" :clinical-trial-isrctn]]]

  ["Deutsches Register Klinischer Studien (DRKS) DRKS00005684" [["drks00005684" :clinical-trial-drks]]]

  ["DMR97-IRB-259" []]

  ["drks-nue.uniklinik-freiburg.de DRKS00003185" [["drks00003185" :clinical-trial-drks]]]

  ["DRKS00003577" [["drks00003577" :clinical-trial-drks]]]

  ["Dutch Cochrane Centre NTR1093" [["ntr1093" :clinical-trial-dutch-trial-register]]]

  ["Dutch Trial Register NTR number 1867 http://www.trialregister.nl/trialreg/admin/rctview.asp?TC=1867" []]

  ["Dutch Trial Register NTR-2051 www.trialregister.nl/trialreg/admin/rctview.asp?TC=2051" [["ntr-2051" :clinical-trial-dutch-trial-register]]]

  ["Dutch Trial Register NTR1813" [["ntr1813" :clinical-trial-dutch-trial-register]]]

  ["Dutch Trial Register NTR1975." [["ntr1975" :clinical-trial-dutch-trial-register]]]

  ["Dutch Trial Register NTR854 http://www.trialregister.nl/trialreg/admin/rctview.asp?TC=854" [["ntr854" :clinical-trial-dutch-trial-register]]]

  ["Effect of vasoactive drugs on esophageal variceal hemodynamics in patients with portal hypertension. Chinese Clinical Trial Registry –TRC-08000252." []]

  ["Ensaiosclinicos.gov.br/RBR-9B5DH7" [["rbr-9b5dh7" :clinical-trial-rebec]]]

  ["EU Clinical Trial Register EUDRACT number 2008-006188-35 https://www.clinicaltrialsregister.eu/ctr-search/trial/2008-006188-35/IT" []]

  ["EU Clinical Trials Database 2011-000375-13 Dutch Trial Register NTR3521" [["ntr3521" :clinical-trial-dutch-trial-register]]]

  ["EU Clinical Trials Register (EU-CTR); EudraCT number 2009-011742-26; www.clinicaltrialsregister.eu/ctr-search/trial/2009-011742-26/AT" []]

  ["EU Clinical Trials Register EudraCT 2006-002065-39</url>" []]

  ["EU Clinical Trials Register; Eudract-Nr: 2006-004639-31" []]

  ["EudraCT 2006-004937-13" []]

  ["EudraCT 2007-003211-31 ClinicalTrials.gov 00789763" []]

  ["EudraCT 2007-004590-25." []]

  ["EudraCT 2013-003020-36 and ClinicalTrials.gov (number not assigned)" []]

  ["EudraCT Clinical Trial Registry 2012-005292-14" []]

  ["EUDRACT: 2007-006448-23 and ISRCTN04857074." [["isrctn04857074" :clinical-trial-isrctn]]]

  ["European Union Clinical Trials Register EudraCT 2008-007178-39" []]

  ["European Union EudraCT database 2007-002916-24 https://www.clinicaltrialsregister.eu/ctr-search/search?query=2007-002916-24 ClinicalTrials.gov NCT01339169 http://clinicaltrials.gov/ct2/show/NCT01339169?term=yf476&rank=5" [["nct01339169" :clinical-trial-clinical-trials-gov] ["nct01339169" :clinical-trial-clinical-trials-gov]]]

  ["FEATURE Clinical Trials.gov NCT00673920" [["nct00673920" :clinical-trial-clinical-trials-gov]]]

  ["Fever is typically treated empirically in rural Mozambique. We examined the distribution and antimicrobial susceptibility patterns of bacterial pathogens isolated from blood-culture specimens, and clinical characteristics of ambulatory HIV-infected febrile patients with and without bacteremia. This analysis was nested within a larger prospective observational study to evaluate the performance of new Mozambican guidelines for fever and anemia in HIV-infected adults (clinical trial registration NCT01681914, www.clinicaltrials.gov)" [["nct01681914" :clinical-trial-clinical-trials-gov]]]

  ["FILM Clinical Trials.gov NCT00485589" [["nct00485589" :clinical-trial-clinical-trials-gov]]]

  ["German Clinical Trial Register DRKS 00000723" [["drks00000723" :clinical-trial-drks]]]

  ["German Clinical Trial Register: Registration Trial DRKS00005954." [["drks00005954" :clinical-trial-drks]]]

  ["German Clinical Trials Organization DRKS00003466" [["drks00003466" :clinical-trial-drks]]]

  ["German Clinical Trials Register (DKRS) DRKS00005587" [["drks00005587" :clinical-trial-drks]]]

  ["German Clinical Trials Register DRKS00000755." [["drks00000755" :clinical-trial-drks]]]

  ["GlaxoSmithKline Clinical Study Register #113734" []]

  ["HIV Prevention Trials Network HPTN 068" []]

  ["HKClinicalTrial.com HKCTR-1343" [["hkctr-1343" :clinical-trial-hkctr]]]

  ["HKClinicalTrials.com HKCTR-365" [["hkctr-365" :clinical-trial-hkctr]]]

  ["http://clinicaltrials.gov/ NCT00755066" [["nct00755066" :clinical-trial-clinical-trials-gov]]]

  ["http://clinicaltrials.gov/ct2/show/NCT01361932?term=nct01361932&rank=1" [["nct01361932" :clinical-trial-clinical-trials-gov]]]

  ["http://www.chictr.org/cn/ ChiCTR-TRC-10000886" [["chictr-trc-10000886" :clinical-trial-chictr]]]

  ["http://www.ClinicalTrials.gov NCT01411865 and NCT01026688" [["nct01411865" :clinical-trial-clinical-trials-gov] ["nct01026688" :clinical-trial-clinical-trials-gov]]]

  ["International Clinical Trials Registry NCT00817128" [["nct00817128" :clinical-trial-clinical-trials-gov]]]

  ["International Standard Randomised Controlled Trial 24953404" []]

  ["Iranian Registry of Clinical Trials IRCT2013061813706N1." [["irct2013061813706n1" :clinical-trial-irct]]]

  ["irct.ir IRCT2014012816395N1" [["irct2014012816395n1" :clinical-trial-irct]]]

  ["ISRCTN 33885819." [["isrctn33885819" :clinical-trial-isrctn]]]

  ["ISRCTN 64808733 UK CRN Portfolio 6470" [["isrctn64808733" :clinical-trial-isrctn]]]

  ["ISRCTN 78288224 – doi10.1186/ISRCTN35836727; UKCRN 4814." [["isrctn35836727" :clinical-trial-isrctn] ["isrctn78288224" :clinical-trial-isrctn] ["ukcrn4814" :clinical-trial-ukcrn]]]

  ["ISRCTN ISRCTN67270384" [["isrctn67270384" :clinical-trial-isrctn]]]

  ["ISRCTN Register 51695599" []]

  ["ISRCTN registry of clinical trials: ISRCTN43070564." [["isrctn43070564" :clinical-trial-isrctn]]]

  ["ISRCTN.com ISRCTN6696362166963621" [["isrctn6696362166963621" :clinical-trial-isrctn]]]

  ["ISRCTN.org ISRCTN50717094" [["isrctn50717094" :clinical-trial-isrctn]]]

  ["ISRCTN40976937." [["isrctn40976937" :clinical-trial-isrctn]]]

  ["ISRCTN77569430." [["isrctn77569430" :clinical-trial-isrctn]]]

  ["ISRTCN Registry: ISRCTN79261738." [["isrctn79261738" :clinical-trial-isrctn]]]

  ["ITEudraCT 2007-002460-98" []]

  ["Japan Clinical Trials Register (http://www.umin.ac.jp/ctr/ number, UMIN 000012527)." [["umin000012527" :clinical-trial-umin-japan]]]

  ["MDMA & PSB NTR 2636" [["ntr2636" :clinical-trial-dutch-trial-register]]]

  ["MitraClip Registry NCT02033811" [["nct02033811" :clinical-trial-clinical-trials-gov]]]

  ["NCT00001137 at www.clinicaltrials.gov." [["nct00001137" :clinical-trial-clinical-trials-gov]]]

  ["NCT00715052" [["nct00715052" :clinical-trial-clinical-trials-gov]]]

  ["NCT00906178." [["nct00906178" :clinical-trial-clinical-trials-gov]]]

  ; Too unformatted.
  ["Nederlands Trial Register (NTR) 1658" []]

  ; Too vague!
  ["Nederlands Trial Register 1120" []]

  ["Nederlands Trial Register 1345." []]

  ["Nederlands Trial Register ISRCTN 52566874" [["isrctn52566874" :clinical-trial-isrctn]]]

  ["Nederlands Trial Register ISRCTN46326316" [["isrctn46326316" :clinical-trial-isrctn]]]

  ["Netherlands Trial Register NTR2627 (date registered 22nd November 2010), and NTR2697 (date registered 13th January 2011)." [["ntr2627" :clinical-trial-dutch-trial-register] ["ntr2697" :clinical-trial-dutch-trial-register]]]

  ["Netherlands Trial Register: NTR1946." [["ntr1946" :clinical-trial-dutch-trial-register]]]

  ; Unrecognised prefix. Maybe meant 'nct'?
  ["NTC00699868." []]

  ["NTR 2473" [["ntr2473" :clinical-trial-dutch-trial-register]]]

  ["Number Eudract 2008-001338-28; ClinicalTrials.gov: NCT00657397; International Standard Randomized Controlled Trial Number Register ISRCTN31125511" [["nct00657397" :clinical-trial-clinical-trials-gov] ["isrctn31125511" :clinical-trial-isrctn]]]

  ["Pactr.org PACTR2010020001771828" [["pactr2010020001771828" :clinical-trial-pactr]]]

  ["Pactr.org PACTR201008000221638" [["pactr201008000221638" :clinical-trial-pactr]]]

  ["Parent Study: ClinicalTrials.gov NCT00342355" [["nct00342355" :clinical-trial-clinical-trials-gov]]]

  ["Pediatric populations continue to be understudied in clinical drug trials despite the increasing use of pharmacotherapy in children" []]

  ["Please see later in the article for the Editors' Summary." []]

  ["Portal of Clinical Research with Medicines in Italy 2004&ndash;004693&ndash;87" []]

  ["PRINCE and TNT are not registered. CAP is registered at Clinicaltrials.gov NCT00451828" [["nct00451828" :clinical-trial-clinical-trials-gov]]]

  ["Promoting Parental Skills and Enhancing Attachment in Early Childhood (CAPEDP)" []]

  ["PROSPERO 2011:CRD42011001329." [["crd42011001329" :clinical-trial-prospero]]]

  ["PROSPERO CRD42012002393" [["crd42012002393" :clinical-trial-prospero]]]

  ["PROSPERO No: CRD42013003721" [["crd42013003721" :clinical-trial-prospero]]]

  ["PROSPERO Register CRD42012002541" [["crd42012002541" :clinical-trial-prospero]]]

  ["PROSPERO Registration #: CRD42013003621." [["crd42013003621" :clinical-trial-prospero]]]

  ["Protocol IAVI VRC V001 [1]. ClinicalTrials.gov NCT00124007 Protocol IAVI 010 [2] (registration with ClincalTrials.gov is in progress)" [["nct00124007" :clinical-trial-clinical-trials-gov]]]

  ["Protocol publication: http://www.trialsjournal.com/content/12/1/43" []]

  ["Protocols IAVI 002 and IAVI 004 are Phase 1 trials only mentioned in introductory paragraphs; details will not be reported. Registration was not required when they were conducted." []]

  ["Rafael Dal-Ré and colleagues argue that the recruitment targets and performance of all site investigators in multi-centre clinical trials should be disclosed in trial registration sites before a trial starts, and when it ends." []]

  ["Reflecting on the new WHO statement on clinical trial registration and reporting, Ben Goldacre proposes simple solutions for ensuring clinical trial results are made publicly available." []]

  ["Registered at clinicaltrials.gov as NCT00655902" [["nct00655902" :clinical-trial-clinical-trials-gov]]]

  ["SA National Health Research Database DOH-27-0207-1539; Clinicaltrials.gov NCT00413725" [["nct00413725" :clinical-trial-clinical-trials-gov]]]

  ["SCRIPT Clinical Trials.gov NCT00476996" [["nct00476996" :clinical-trial-clinical-trials-gov]]]

  ["STAGE Clinical Trials.gov NCT00406419" [["nct00406419" :clinical-trial-clinical-trials-gov]]]

  ["Swiss Medical Registry 2003DR2327; ClinicalTrials.gov NCT00369616" [["nct00369616" :clinical-trial-clinical-trials-gov]]]

  ["Swissmedic.ch 2002 DR 1227" []]

  ["The Australian Clinical Trials Registry ACTRN12607000387426" [["actrn12607000387426" :clinical-trial-actrn]]]

  ["The Brazilian Clinical Trials Registry RBR-8854CD" [["rbr-8854cd" :clinical-trial-rebec]]]

  ["The EU Clinical Trials Register 2009-011723-31" []]

  ["The Gene Expression Omnibus (GEO) GSE31210" []]

  ["The Japan Medical Association Clinical Trial Registry (JMACCT)JMA-IIA00069" [["jma-iia00069" :clinical-trial-jma]]]

  ["The Netherlands National Trial Register NTR2605" [["ntr2605" :clinical-trial-dutch-trial-register]]]

  ["The Pan African Clinical Trials Registry ATMR2009040001075080 (currently PACTR2009040001075080)" [["pactr2009040001075080" :clinical-trial-pactr]]]

  ["The Pan African Clinical Trials Registry PACTR2008120000904116" [["pactr2008120000904116" :clinical-trial-pactr]]]

  ["The review protocol was registered in the PROSPERO database before the start of the review process (CRD number 42013006565)." []]

  ["The study is not a randomized controlled trial and was not registered." []]

  ["The study is registered at ClinicalTrials.gov NCT01601444" [["nct01601444" :clinical-trial-clinical-trials-gov]]]

  ["The study is registered at ClinicalTrials.gov under identifier: NCT00740584" [["nct00740584" :clinical-trial-clinical-trials-gov]]]

  ["The study was registered at ClinicalTrials.gov NCT00573781." [["nct00573781" :clinical-trial-clinical-trials-gov]]]

  ["The study was registered in the Clinical Trial Register under the ClinicalTrials.gov Identifier NCT01491282." [["nct01491282" :clinical-trial-clinical-trials-gov]]]

  ["The study was registered on Chinese Clinical Trial Registry as ChiCTR-TRC-13003806." [["chictr-trc-13003806" :clinical-trial-chictr]]]

  ["The study was registered with the Australian New Zealand Clinical Trials Registry; ACTRN12611000963921 on 8th November 2011." [["actrn12611000963921" :clinical-trial-actrn]]]

  ["The University Hospital Medical Information Network (UMIN) Clinical Trials Registry UMIN000004678" [["umin000004678" :clinical-trial-umin-japan]]]

  ["This analysis is based on secondary data obtained from three clinical trials. ClinicalTrials.gov. NCT00386230, NCT00398684, NCT00409591." [["nct00386230" :clinical-trial-clinical-trials-gov] ["nct00398684" :clinical-trial-clinical-trials-gov] ["nct00409591" :clinical-trial-clinical-trials-gov]]]

  ["This clinical trial is registered on Clinicaltrials.gov, registry number NCT00931463." [["nct00931463" :clinical-trial-clinical-trials-gov]]]

  ["This paper uses data collected by the CRASH 2 trial: Controlled-Trials.com ISRCTN86750102, Clinicaltrials.gov NCT00375258 and South African Clinical Trial Register DOH-27-0607-1919." [["isrctn86750102" :clinical-trial-isrctn] ["nct00375258" :clinical-trial-clinical-trials-gov]]]

  ["This paper uses data collected in a previous clinical trial registered at the Australian Clinical Trials Registry, Australian New Zealand Clinical Trials Registry: Anzcrt.org.au ACTRN012607000195459" [["actrn012607000195459" :clinical-trial-actrn]]]

  ; messed up identifier
  ["This randomised, double blind, parallel group, placebo controlled trial is registered with the International Standard Randomised Controlled Trials Register, Number (ISRCTN) 26287422. Registered title: Probiotics in the prevention of atopy in infants and children." []]

  ["This study did not meet Peruvian and some other international criteria for a clinical trial but was registered with the ClinicalTrials.gov registry: ClinicalTrials.gov NCT00054769" [["nct00054769" :clinical-trial-clinical-trials-gov]]]

  ["This study is publically registered on the United Kingdom Clinical Research Network Portfolio (9633)." []]

  ["This study was conducted under the NIDDK protocol 06-DK-0036 and is listed in ClinicalTrials.gov NCT00261898" [["nct00261898" :clinical-trial-clinical-trials-gov]]]

  ["This trial is registered at clinical trials.gov (identifier: NCT00915252)." [["nct00915252" :clinical-trial-clinical-trials-gov]]]

  ["This trial is registered in ClinicalTrials.gov, with the Name: Effect of Higher Doses of Remifentanil on Postoperative Pain in Patients Undergoing Thyroidectomy, and ID number: NCT01761149." [["nct01761149" :clinical-trial-clinical-trials-gov]]]

  ["This trial is registered with number NCT00938379" [["nct00938379" :clinical-trial-clinical-trials-gov]]]

  ["This trial was registered with the Australian New Zealand Clinical Trials Registry: ACTRN12610000604000 (http://www.anzctr.org.au/TrialSearch.aspx)." [["actrn12610000604000" :clinical-trial-actrn]]]

  ["Trial A: Nederlands Trial Register NTR1907 Trial B: Nederlands Trial Register NTR2503" [["ntr1907" :clinical-trial-dutch-trial-register] ["ntr2503" :clinical-trial-dutch-trial-register]]]

  ["Trial Registration UMIN Clinical Trials Registry (UMIN-CTR) UMIN000010159" [["umin000010159" :clinical-trial-umin-japan]]]

  ["Trial Registration: Australia New Zealand Clinical Trials Registry ACTRN12611000067976 www.anzctr.org.au" [["actrn12611000067976" :clinical-trial-actrn]]]

  ["Trial Registration: Clinical Trials.gov registration: NCT01014455" [["nct01014455" :clinical-trial-clinical-trials-gov]]]

  ["Trial Registration: ClinicalTrials.gov NCT00379548" [["nct00379548" :clinical-trial-clinical-trials-gov]]]

  ["Trial Registration: International Clinical Trials Registry Platform NTR2926 http://apps.who.int/trialsearch/" [["ntr2926" :clinical-trial-dutch-trial-register]]]

  ["Trial Registration: UMIN-CTR UMIN000004997" [["umin000004997" :clinical-trial-umin-japan]]]

  ["Trial Registration: www.pactr.org PACTR201303000464219)" [["pactr201303000464219" :clinical-trial-pactr]]]

  ["Trial Registrations: ClinicalTrials.gov NCT01006980;" [["nct01006980" :clinical-trial-clinical-trials-gov]]]

  ["trialregister.nl 1230" []]

  ["TrialRegister.nl NTR1193" [["ntr1193" :clinical-trial-dutch-trial-register]]]

  ["TrialRegister.nl NTR1609 <rctview.asp&quest;TC&hairsp;&equals;&hairsp;1609>" [["ntr1609" :clinical-trial-dutch-trial-register]]]

  ["trialregister.nl NTR2728. Registry name: improving executive functioning in children with ADHD: training executive functions within the context of a computer game; registry number: NTR2728." [["ntr2728" :clinical-trial-dutch-trial-register] ["ntr2728" :clinical-trial-dutch-trial-register]]]

  ["TrialRegister.nl NTR3170" [["ntr3170" :clinical-trial-dutch-trial-register]]]

  ["Trialregister.nl NTR721 (ISRCTN Registry: ISRCTN42786336)" [["ntr721" :clinical-trial-dutch-trial-register] ["isrctn42786336" :clinical-trial-isrctn]]]

  ["TrialRegister.nl NTR965" [["ntr965" :clinical-trial-dutch-trial-register]]]

  ["Trialregister.nl/trialreg/index.asp. NTR1974" [["ntr1974" :clinical-trial-dutch-trial-register]]]

  ["Trialregitser.nl NTR1040" [["ntr1040" :clinical-trial-dutch-trial-register]]]

  ["UK Clinical Research Network ID 8877 ISRCTN17030426 www.isrctn.com" [["isrctn17030426" :clinical-trial-isrctn]]]

  ["UK Clinical Research Network UKCRN 6590" [["ukcrn6590" :clinical-trial-ukcrn]]]

  ["UMIN 000006081." [["umin000006081" :clinical-trial-umin-japan]]]

  ["UMIN clinical trial center UMIN000004803" [["umin000004803" :clinical-trial-umin-japan]]]

  ["UMIN Clinical Trial Registry UMIN000001748" [["umin000001748" :clinical-trial-umin-japan]]]

  ["UMIN Clinical Trials Registry (UMIN-CTR) UMIN000012148" [["umin000012148" :clinical-trial-umin-japan]]]

  ["UMIN Clinical Trials Registry UMIN000002265" [["umin000002265" :clinical-trial-umin-japan]]]

  ["UMIN UMIN000001835" [["umin000001835" :clinical-trial-umin-japan]]]

  ["UMIN-CTR (https://upload.umin.ac.jp/cgi-open-bin/ctr/ctr.cgi?function=search&action=input&language=E) UMIN000001102" [["umin000001102" :clinical-trial-umin-japan]]]

  ["UMIN-CTR UMIN000003286" [["umin000003286" :clinical-trial-umin-japan]]]

  ["UMIN-CTR UMIN000003662 ctr.cgi&quest;function&hairsp;&equals;&hairsp;brows&amp;action&hairsp;&equals;&hairsp;brows&amp;type&hairsp;&equals;&hairsp;summary&amp;recptno&hairsp;&equals;&hairsp;R000004436&amp;language&hairsp;&equals;&hairsp;J." [["umin000003662" :clinical-trial-umin-japan]]]

  ["UMIN-CTR UMIN000013172" [["umin000013172" :clinical-trial-umin-japan]]]

  ["UMIN-CTR UMIN000016169" [["umin000016169" :clinical-trial-umin-japan]]]

  ["Umin.ac.jp UMIN-CTR000008048" [["umin-ctr000008048" :clinical-trial-umin-japan]]]

  ["UMIN.ac.jp UMIN000001383" [["umin000001383" :clinical-trial-umin-japan]]]

  ["UMIN.ac.jp UMIN000003580." [["umin000003580" :clinical-trial-umin-japan]]]

  ["UMIN.ac.jp/ctr/UMIN000002410" [["umin000002410" :clinical-trial-umin-japan]]]

  ["UMIN000009614" [["umin000009614" :clinical-trial-umin-japan]]]

  ["UMIN000010500" [["umin000010500" :clinical-trial-umin-japan]]]

  ["University hospital Medical Information Network (UMIN) UMIN000003760" [["umin000003760" :clinical-trial-umin-japan]]]

  ["University hospital Medical Information Network trial registry Reg. no. R000001298, Trial ID UMIN000001070." [["umin000001070" :clinical-trial-umin-japan]]]

  ["University hospital Medical Information Network UMIN No. 000012915" []]

  ["Verband Forschender Arzneimittelhersteller e.V., Berlin, Germany ML21645 ClinicalTrials.gov NCT02106156" [["nct02106156" :clinical-trial-clinical-trials-gov]]]

  ["World Health Organization International Clinical Trials Registry Platform PACTR2010050002122368" [["pactr2010050002122368" :clinical-trial-pactr]]]

  ["www.anzctr.org.au ACTRN12611000410954" [["actrn12611000410954" :clinical-trial-actrn]]]

  ["www.ClinicalTrials.gov NCT00548379 https://www.clinicaltrials.gov/ct2/show/NCT00548379" [["nct00548379" :clinical-trial-clinical-trials-gov]]]

  ["www.clinicaltrials.gov. NCT01360788 and NCT01072396." [["nct01360788" :clinical-trial-clinical-trials-gov] ["nct01072396" :clinical-trial-clinical-trials-gov]]]

  ["www.controlled-trials.com ISRCTN 27354239. isrctn27354239" [["isrctn27354239" :clinical-trial-isrctn]]]

  ; Should work with the space.
  ["www.Controlled-Trials.com ISRCTN 27657880." [["isrctn27657880" :clinical-trial-isrctn]]]

  ["www.ctri.nic.in 004236" []]

  ["www.germanctr.de DRKS00005657" [["drks00005657" :clinical-trial-drks]]]

  ["www.germanctr.de/, DRKS00005062" [["drks00005062" :clinical-trial-drks]]]

  ["www.ISRCTN.org ISRCTN-04252749." [["isrctn-04252749" :clinical-trial-isrctn]]]

  ["www.slctr.lk SLCTR/2007/005" [["slctr/2007/005" :clinical-trial-slctr]]]
                  
                  ])

(deftest extract-plos
  (testing "Get as many meaningful idenifiers out of PLOS metadata."
    (doseq [[input expected] plos-inputs]
      (let [result (util/extract-cts input)]
        (is (= (set result) (set expected)))
        
        
        )
      
      )))
