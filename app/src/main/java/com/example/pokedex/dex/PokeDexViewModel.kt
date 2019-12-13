package com.example.pokedex.dex

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pokedex.data.PokeRepository
import com.example.pokedex.model.PokeResult
import com.example.pokedex.model.Pokemon
import com.example.pokedex.model.PokemonRef
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
                error.value = t.message
                println("Failed to get pokemons! ${t.message}")
            }
        })
    }

    fun getPokemon(url: String) {
        pokeRepository.getPokemon(url).enqueue(object : Callback<Pokemon> {
            override fun onResponse(call: Call<Pokemon>, response: Response<Pokemon>) {
                if (response.isSuccessful) {
                    pokemon.value = response.body()
                }
                else error.value = "An error occurred: ${response.errorBody().toString()}"
            }

            override fun onFailure(call: Call<Pokemon>, t: Throwable) {
                errorPoke.value = t.message
                println("Failed to get pokemon! ${t.message}")
            }
        })
    }

}
