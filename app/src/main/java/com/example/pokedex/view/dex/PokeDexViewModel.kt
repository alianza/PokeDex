package com.example.pokedex.view.dex

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pokedex.model.data.pokeApi.PokeRepository
import com.example.pokedex.model.entity.PokeResult
import com.example.pokedex.model.entity.Pokemon
import com.example.pokedex.model.entity.PokemonRef
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PokeDexViewModel : ViewModel() {

    private val pokeRepository = PokeRepository()
    val pokemonRefs = MutableLiveData<Array<PokemonRef>>()
    val pokemon = MutableLiveData<Pokemon>()
    val error = MutableLiveData<String>()
    val errorPoke = MutableLiveData<String>()

    fun getPokemonRefs() {
        pokeRepository.getPokemonRefs().enqueue(object : Callback<PokeResult> {
            override fun onResponse(call: Call<PokeResult>, response: Response<PokeResult>) {
                if (response.isSuccessful) {
                    pokemonRefs.value = response.body()?.pokemons
                }
                else error.value = "An error occurred: ${response.errorBody().toString()}"
            }

            override fun onFailure(call: Call<PokeResult>, t: Throwable) {
                error.value = "Failed to get pokemons! ${t.message}"
                println(error.value)
            }
        })
    }

    fun getPokemon(url: String) {
        pokeRepository.getPokemon(url).enqueue(object : Callback<Pokemon> {
            override fun onResponse(call: Call<Pokemon>, response: Response<Pokemon>) {
                if (response.isSuccessful) {
                    val pokemonResult = response.body() as (Pokemon)
                    // Only low level pokemon and no 'alola's' (they have no poster)
//                    if (pokemonResult.base_experience <= 100f && !pokemonResult.name.contains("alola")) {
                        pokemon.value = pokemonResult
//                    }
                }
                else error.value = "An error occurred: ${response.errorBody().toString()}"
            }

            override fun onFailure(call: Call<Pokemon>, t: Throwable) {
                errorPoke.value = "Failed to get pokemon! ${t.message}"
                println(errorPoke.value)
            }
        })
    }
}
