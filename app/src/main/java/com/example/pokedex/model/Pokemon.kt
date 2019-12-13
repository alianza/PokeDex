package com.example.pokedex.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Pokemon (
    @SerializedName("name") var name: String,
    @SerializedName("height") var height: String,
    @SerializedName("base_experience") var base_experience: Double,
    @SerializedName("sprites") var sprites: Sprites,
    @SerializedName("order") var order: Int
    ): Parcelable {

    @Parcelize
    data class Sprites (
        @SerializedName("front_default") var poster: String
    ): Parcelable
}