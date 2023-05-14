# Retseptirakendus
Android Studio Electric Eel | 2022.1.1, SDK 29, 33, Kotlin


## Rakenduse arhitektuur
Rakendus põhineb Model–View–Viewmodel (MVVM) lahendusel, mis võimaldab andmete haldamise lahutada kasutusliidesest. Rakendusel on üks Activity, mille sees vahetuvad rakenduses navigeerimisel sisufragmendid. Kõik fragmendid kasutavad andmete saamiseks ja nende töötluseks ühist ViewModel'it (SharedViewModel), mille kaudu käib andmerepositooriumi haldamine. See tagab nii sujuva andmete liikumise kui ka andmete säilimise rakenduses navigeerides.


## Server
Retsepte hoitakse MondoDB andmebaasis ja neile saadakse ligi Cyclic.sh serveri abil, mille koodi majutatakse Githubis (https://github.com/knyrr/recipe-api). 


## Avaleht

## Retseptiloend
Retseptiloendis kuvatakse kõiki andmebaasis esinevaid retsepte. Andmed on esitatud RecyclerView abil, mis seob andmeloendi sisu ViewHolderiga. Retsepte on võimalik märkida lemmikuks. Sellisel juhul salvestatakse SharedViewModel kaudu valitud reptsepti id SharedPreferences all asuvasse serialiseeritud loendisse. Kui rakendust avades tõmmatakse serverist retseptid, siis kontrollitakse, kas need on märgitud kohalikuks lemmikuks ja lisatakse andmeobjektile vastav info (RecipeDto.fave=true). Sarnaselt toimetatakse ka ostukärru lisamisel. 
Retseptile vajutades avaneb detailne vaade.

## Retsepti detailne vaade 

## Retsepti lisamine

## Ostukäru



