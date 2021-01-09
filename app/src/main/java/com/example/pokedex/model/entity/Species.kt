package com.example.pokedex.model.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Species (
    @SerializedName("flavor_text_entries") var flavor_text_entries: Array<FlavorText>,
    @SerializedName("genera") var genera: Array<Genus>
    ): Parcelable {

    @Parcelize
    data class Genus (
        @SerializedName("genus") var genus: String,
        @SerializedName("language") var language: Language
    ): Parcelable

    @Parcelize
    data class FlavorText (
        @SerializedName("flavor_text") var text: String,
        @SerializedName("language") var language: Language
    ): Parcelable

    @Parcelize
    data class Language (
        @SerializedName("name") var name: String,
        @SerializedName("url") var url: String
    ): Parcelable
}