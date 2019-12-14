package com.example.pokedex.data

import com.example.pokedex.model.PokeResult
import com.example.pokedex.model.Pokemon
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface PokeApiService {
        @GET("pokemon/?offset=0&limit=100")
        fun getAllPokemonRefs(): Call<PokeResult>

        @GET("pokemon/{url}")
        fun getPokemon(@Path(value = "url", encoded = true) url: String): Call<Pokemon>
}