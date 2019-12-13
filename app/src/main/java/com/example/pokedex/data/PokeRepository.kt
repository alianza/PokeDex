package com.example.pokedex.data

import com.example.pokedex.model.PokeResult
import com.example.pokedex.model.Pokemon
import retrofit2.Call

class PokeRepository {

    private val pokeApi: PokeApiService = PokeApi.createApi()

    fun getPokemonRefs(): Call<PokeResult> = pokeApi.getAllPokemonRefs()

    fun getPokemon(url: String): Call<Pokemon> = pokeApi.getPokemon(url)
}