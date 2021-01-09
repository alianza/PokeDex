package com.example.pokedex.model.data.pokeApi

import com.example.pokedex.model.entity.PokeResult
import com.example.pokedex.model.entity.Pokemon
import com.example.pokedex.model.entity.Species
import retrofit2.Call

class PokeRepository {

    private val pokeApi: PokeApiService = PokeApi.createApi()

    fun getPokemonRefs(params: Map<String, Int>): Call<PokeResult> = pokeApi.getAllPokemonRefs(params)

    fun getTotalNumberOfPokemon(): Call<PokeResult> = pokeApi.getTotalNumberOfPokemon()

    fun getPokemon(url: String): Call<Pokemon> = pokeApi.getPokemon(url)

    fun getSpecies(url: String): Call<Species> = pokeApi.getSpecies(url)
}