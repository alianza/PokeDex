package com.example.pokedex.model.data.pokeApi

import com.example.pokedex.BuildConfig
import com.example.pokedex.model.entity.PokeResult
import com.example.pokedex.model.entity.Pokemon
import com.example.pokedex.model.entity.Species
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface PokeApiService {
        @GET("pokemon/?offset=0&limit=${BuildConfig.POKEMONS_TO_LOAD}")
        fun getAllPokemonRefs(): Call<PokeResult>

        @GET("pokemon/{url}")
        fun getPokemon(@Path(value = "url", encoded = true) url: String): Call<Pokemon>

        @GET("pokemon-species/{url}")
        fun getSpecies(@Path(value = "url", encoded = true) url: String): Call<Species>
}