package com.example.pokedex.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PokemonRef (
    @SerializedName("name") var name: String,
    @SerializedName("url") var url: String
    ): Parcelable