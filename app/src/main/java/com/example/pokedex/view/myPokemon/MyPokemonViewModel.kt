package com.example.pokedex.view.myPokemon

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.pokedex.model.data.room.SavedPokeRepository

class MyPokemonViewModel(application: Application) : AndroidViewModel(application) {

    private val savedPokeRepository = SavedPokeRepository(application.applicationContext)

    var savedPokemon = savedPokeRepository.getPokemons()

}
