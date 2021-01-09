package com.example.pokedex.model.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Suppress("ArrayInDataClass")
@Parcelize
data class PokeResult (
    @SerializedName("results") var pokemons: Array<PokemonRef>
): Parcelable
