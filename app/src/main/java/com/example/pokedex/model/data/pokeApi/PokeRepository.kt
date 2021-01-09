package com.example.pokedex.model.data.pokeApi

import com.example.pokedex.model.entity.PokeResult
import com.example.pokedex.model.entity.Pokemon
import com.example.pokedex.model.entity.Species
import retrofit2.Call

class PokeRepository {

    private val pokeApi: PokeApiService = PokeApi.createApi()

    fun getPokemonRefs(): Call<PokeResult> = pokeApi.getAllPokemonRefs()

    fun getPokemon(url: String): Call<Pokemon> = pokeApi.getPokemon(url)

    fun getSpecies(url: String): Call<Species> = pokeApi.getSpecies(url)
}