package com.example.pokedex.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.pokedex.data.pokeApi.PokeRepository
import com.example.pokedex.data.room.SavedPokeRepository
import com.example.pokedex.model.Pokemon
import com.example.pokedex.model.SavedPokemon
import com.example.pokedex.model.Species
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(application: Application) : AndroidViewModel(application) {

    private val pokeRepository = PokeRepository()
    val species = MutableLiveData<Species>()
    val pokemon = MutableLiveData<Pokemon>()
    val error = MutableLiveData<String>()
    val errorPoke = MutableLiveData<String>()

    private val savedPokeRepository = SavedPokeRepository(application.applicationContext)
    private val mainScope = CoroutineScope(Dispatchers.Main)

    lateinit var savedPokemon: LiveData<SavedPokemon?>

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

    fun getSpecies(url: String) {
        pokeRepository.getSpecies(url).enqueue(object : Callback<Species> {
            override fun onResponse(call: Call<Species>, response: Response<Species>) {
                if (response.isSuccessful) {
                    species.value = response.body()
                }
                else error.value = "An error occurred: ${response.errorBody().toString()}"
            }

            override fun onFailure(call: Call<Species>, t: Throwable) {
                errorPoke.value = t.message
                println("Failed to get pokemon! ${t.message}")
            }
        })
    }

    fun insertSavedPokemon(pokemon: SavedPokemon) {
        mainScope.launch {
            savedPokeRepository.insertPokemon(pokemon)
        }
    }

    fun updateSavedPokemon(pokemon: SavedPokemon) {
        mainScope.launch {
            savedPokeRepository.updatePokemon(pokemon)
        }
    }

    fun getSavedPokemon(name: String) {
        savedPokemon = savedPokeRepository.getPokemon(name)
    }
}
