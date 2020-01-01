package com.example.pokedex.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Pokemon (
    @SerializedName("name") var name: String,
    @SerializedName("height") var height: Int,
    @SerializedName("base_experience") var base_experience: Double,
    @SerializedName("weight") var weight: Int,
    @SerializedName("sprites") var sprites: Sprites,
    @SerializedName("order") var order: Int,
    @SerializedName("types") var types: Array<Types>,
    @SerializedName("stats") var stats: Array<Stats>,
    @SerializedName("species") var species: Species
    ): Parcelable {

    @Parcelize
    data class Sprites (
        @SerializedName("front_default") var front_poster: String,
        @SerializedName("back_default") var back_poster: String
    ): Parcelable

    @Parcelize
    data class Types (
        @SerializedName("slot") var slot: Int,
        @SerializedName("type") var type: Type
    ): Parcelable

    @Parcelize
    data class Type (
        @SerializedName("name") var name: String,
        @SerializedName("url") var url: String
    ): Parcelable

    @Parcelize
    data class Stats (
        @SerializedName("base_stat") var base_stat: Int,
        @SerializedName("effort") var effort: Int,
        @SerializedName("stat") var stat: Stat
    ): Parcelable

    @Parcelize
    data class Stat (
        @SerializedName("name") var name: String,
        @SerializedName("url") var url: String
    ): Parcelable

    @Parcelize
    data class Species (
        @SerializedName("name") var name: String,
        @SerializedName("url") var url: String
    ): Parcelable
}