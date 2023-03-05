# Klassid 2
## Andmeklassid
Andmeklassid on mõeldud vaid andmete hoidmiseks, võrdlemiseks ja kopeerimiseks. Kotlin pakub sisseehitatud võimalust enda andmeklasse luua.
```kotlin
data class User(val name: String = "", val age: Int = 99)
```
Sellise koodireaga tekib klass `User`, millel on kohe olemas järgnevad funktsioonid:

`equals()` - kahe `User`'i võrdlemiseks omaduste haaval

`hashCode()` - tagastab arvulise väärtuse `User`'i sisust, kahel samasugusel kasutajal tuleb sama arv, ehk saab ka neid võrrelda

`toString()` - tagastab `String`'i kasutaja sisust, kujul `"User(name=John, age=43)"`

`componentN()` - muutujate destruktureerimiseks, nt

```kotlin
val person = User("admin", 123)
val (name, age) = person
/*
Ülalolev koodirida kompileerub järgnevaks:
  val name = person.component1()
  val age = person.component2()
*/
```

`copy()` - saab ühe kasutaja andmed teisele lihtsasti kopeerida ning muuta

## Enum klassid
Enum klasside eesmärk on pakkuda arendajale loetavaid ja mõistetavaid konstantseid väärtuseid, mis on tegelikult masinkoodis vaikimisi hoopis arvu kujul.

```kotlin
enum class Direction {
    NORTH,
    EAST
    SOUTH,
    WEST
}
```

On võimalik luua ka enum klass, millel on olemas erinevad konstandid, kuid saab ka neist erinevat väärtust kasutada.

```kotlin
enum class Color(val rgb: Int) {
    RED(0xFF0000),
    GREEN(0x00FF00),
    BLUE(0x0000FF)
}
```

Anonüümseid klasse ja abstraktseid funktsioone kasutades saab ka midagi sellist luua, mis on tavalise enum klassiga võrreldes juba päris erinev.

```kotlin
enum class ProtocolState {
    WAITING {
        override fun signal() = TALKING
    },
    TALKING {
        override fun signal() = WAITING
    };
    
    abstract fun signal(): ProtocolState
}
```

## Objektid

Vahest on vaja klassi funktsionaalsusi, aga pole vaja eraldi klassi tekitada. Siin tulevad objektid appi, millega saab luua anonüümseid klasse.

```kotlin
val helloWorld = object {
    val hello = "Hello"
    val world = "World"

    override fun toString() = "$hello $world"
}
```

Anonüümne klass saab ka pärineda mingist olemasolevast klassist.

```kotlin
val example = object: User() {
    override fun toString(): String {
        /* ... */
    }
}
```

## Delegatsioon

Delegatsiooniga on võimalik edasi arendada erinevaid klasse, mis kõik täidavad mingi *interface*/liidese nõudeid.

```kotlin
interface Software {
    fun getLicense(): String
}

class OpenSource(): Software {
    override fun getLicense(): String {
        return "Open Source Software"
    }
}

class ClosedSource(): Software {
    override fun getLicense(): String {
        return "Closed Source Software"
    }
}
```

Kui nüüd tahta edasist funktsionaalsust `Software` liidest täitvale klassile anda, saab seda teha delegatsiooniga.

```kotlin
class PrintableSoftware(sw: Software): Software {
    override fun getLicense(): String {
        return sw.getLicense()
    }

    fun printLicense() {
        print(sw.getLicense())
    }
}

```

Kotlin pakub **by** võtmesõna sama funktsionaalsuse saavutamiseks, mis ülemises koodiplokis.

```kotlin
class PrintableSoftware(sw: Software): Software by sw {
    fun printLicense() {
        print(getLicense())
    }
}
```