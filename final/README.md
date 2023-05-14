# Retseptirakendus

Android Studio Electric Eel | 2022.1.1, SDK 29, 33, Kotlin

## Rakenduse arhitektuur

Rakendus põhineb Model–View–Viewmodel (MVVM) lahendusel, mis võimaldab andmete haldamise lahutada kasutusliidesest. Rakendusel on üks Activity, mille sees vahetuvad rakenduses navigeerimisel sisufragmendid. Kõik fragmendid kasutavad andmete saamiseks ja nende töötluseks ühist ViewModel'it (SharedViewModel), mille kaudu käib andmerepositooriumi haldamine. See tagab nii sujuva andmete liikumise kui ka andmete säilimise rakenduses navigeerides.

## Server

```
https://recipe-api.cyclic.app
```

Tegemist on ühe varasema projekti raames loodud tagarakendusega, mida on kohandatud mobiilrakenduse jaoks. Retsepte hoitakse MondoDB andmebaasis ja neile saadakse ligi Cyclic.sh hostil oleva Node.js serveri abil, mille koodi majutatakse Githubis (https://github.com/knyrr/recipe-api).

### API päringud

#### Tsitaat

```
/quote
```

Tagastab ühe toiduga seotud tsitaadi koos autoriga. Andmed pärinevad NinjaAPI keskkonnast, mille kasutamiseks on serveril API võti olemas.

#### Retsept

```
/recipe
```

Tagastab kõik andmebaasis olevad retseptid - nimi, koostisosad, juhised, kirjeldus, pilt. \
Pildid on kodeeritud base64 formaati, väikese kirjete hulga tõttu on see hetkel toimiv lahendus, kuid kui retseptikirjeid oleks rohkem, siis peaks kindlasti mõtlema eraldi failihalduse peale, sest nende sõnede pikkuste tõttu saavad päringu kiirused kannatada.

```
/recipe/add
```

Lisab andmebaasi uue retsepti kirje.

```
/recipe/:id
```

Kustutab andmebaasist vastava id'ga kirje.

## Avaleht
Avalehel kuvatakse "Päeva retsept" ja tsitaat. Päeva retsept valitakse suvaliselt SharedPreferences'i talletatud retseptide seast. KUvatakse ainult pildi ja retsept nime. Pildi suurus muutub selliselt, et see mahuks ilma pildi suuruste suhet muutmata etteantud alale ära. Tsitaadi alal kuvatakse andmebaasist päringuna saadud suvaline tsitaat. Tsitaadi teksti suurus muutub selliselt, et see mahuks alati etteantud ala piiridesse. Päeva retseptile vajutades avaneb detailne vaade.

## Retseptiloend

Retseptiloendis kuvatakse kõiki andmebaasis esinevaid retsepte. Andmed on esitatud RecyclerView abil, mis seob adapteri abil andmeloendi sisu ViewHolderiga. Andmeid hoitakse repositooriumis LiveData'na, mille muutumist jälgitakse adapteris. Kui andmed muutuvad, siis värskendab adapter automaatselt vaadet.
Retsepte on võimalik märkida lemmikuks. Sellisel juhul salvestatakse SharedViewModel kaudu valitud reptsepti id SharedPreferences all asuvasse serialiseeritud loendisse. Kui rakendust avades tõmmatakse serverist retseptid, siis kontrollitakse, kas need on märgitud kohalikuks lemmikuks ja lisatakse andmeobjektile vastav info (RecipeDto.fave=true). Sarnaselt toimetatakse ka ostukärru lisamisel.
Retseptile vajutades avaneb detailne vaade.

## Retsepti detailne vaade

## Retsepti lisamine

## Ostukäru
