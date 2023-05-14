# Retseptirakendus

Android Studio Electric Eel | 2022.1.1, SDK 29, 33, Kotlin

## Install
Rakenduse katsetamiseks tuleb kõigepealt veenduda, et on olemas sobiv IDE, näiteks Android Studio. Klooni repo ja ava see "final" kaustas. Seadista endale emulaator koos min SDK 29, rohkem infot - https://developer.android.com/studio/run/managing-avds \
Kui need sammud on tehtud, siis lihtsalt "Run App" loodud emulaatoris. \
Oma enda telefonis kasutamiseks tuleb kõigepealt telefonis lubada "Arendaja Valikud", seda saab teha nt nii:
 * Sätted -> Teave telefoni kohta -> Tarkvara teave
 * Järgu number (ing. k. Build number) vajutada 7 korda, ilmub ka popup mis ütleb kui palju vajutusi on jäänud

Nüüd kui Arendaja Valikud on sisse lülitatud, siis saab seadme lisada Android Studiosse:
 * Device Manager
 * Physical -> Pair using WiFi (Telefon peab olema arvutiga samas WiFi võrgus)
 * Telefonis mine Arendaja Valikud - Seadme sidumine QR-koodiga
 * Scan ja rakenduse installimise protsessi peaksid IDEst nägema, tõrgete korral on Google suureks sõbraks


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
Avalehel kuvatakse "Päeva retsept" ja tsitaat. Päeva retsept valitakse suvaliselt SharedPreferences'i talletatud retseptide seast. Kuvatakse ainult pilti ja retsept nime. Pildi suurus muutub selliselt, et see mahuks ilma pildi suuruste suhet muutmata etteantud alale ära. Tsitaadi alal kuvatakse serverist päringuna saadud tsitaat. Tsitaadi teksti suurus muutub selliselt, et see mahuks alati etteantud ala piiridesse. Päeva retseptile vajutades avaneb detailne vaade.

## Retseptiloend

Retseptiloendis kuvatakse kõiki andmebaasis esinevaid retsepte. Andmed on esitatud RecyclerView abil, mis seob adapteri abil andmeloendi sisu ViewHolderiga. Andmeid hoitakse repositooriumis LiveData'na, mille muutumist jälgitakse adapteris. Kui andmed muutuvad, siis värskendab adapter automaatselt vaadet.
Retsepte on võimalik märkida lemmikuks. Sellisel juhul salvestatakse SharedViewModel kaudu valitud reptsepti id SharedPreferences all asuvasse serialiseeritud loendisse. Kui rakendust avades tõmmatakse serverist retseptid, siis kontrollitakse, kas need on märgitud kohalikuks lemmikuks ja lisatakse andmeobjektile vastav info (RecipeDto.fave=true). Sarnaselt toimetatakse ka ostukärru lisamisel.
Retseptile vajutades avaneb detailne vaade.

## Retsepti detailne vaade

## Retsepti lisamine

## Ostukäru

## Teegid
 * Moshi: Teek, mis aitab JSON formaati Java või Kotlini objektideks muuta.
 * Gson: Teek, mis aitab JSON formaati Java või Kotlini objektideks muuta ja vastupidi.
 * Glide: Teek, mis aitab Androidis pildide laadimist hõlbustada.
