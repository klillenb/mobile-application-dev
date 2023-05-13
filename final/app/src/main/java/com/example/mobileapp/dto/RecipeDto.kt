package com.example.mobileapp.dto

import android.os.Parcel
import android.os.Parcelable

data class RecipeDto(
    val _id: String?,
    val name: String?,
    val ingredients: List<String>?,
    val instructions: String?,
    val description: String?,
    val image: String?,
    var fave: Boolean = false,
    var inCart: Boolean = false,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.createStringArrayList(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(_id)
        parcel.writeString(name)
        parcel.writeStringList(ingredients)
        parcel.writeString(instructions)
        parcel.writeString(description)
        parcel.writeString(image)
        parcel.writeByte(if (fave) 1 else 0)
        parcel.writeByte(if (inCart) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RecipeDto> {
        override fun createFromParcel(parcel: Parcel): RecipeDto {
            return RecipeDto(parcel)
        }

        override fun newArray(size: Int): Array<RecipeDto?> {
            return arrayOfNulls(size)
        }
    }
}
