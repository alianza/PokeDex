package com.example.pokedex.data.room

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.pokedex.model.SavedPokemon

class SavedPokeRepository(context: Context) {

    private val savedPokeDao: SavedPokeDao

    init {
        val database = SavedPokemonRoomDatabase.getDatabase(context)
        savedPokeDao = database!!.pokeDao()
    }

    fun getPokemons(): LiveData<List<SavedPokemon>?> {
        return savedPokeDao.getPokemons()
    }

    fun getPokemon(name: String): LiveData<SavedPokemon?> {
        return savedPokeDao.getPokemon(name)
    }

    suspend fun updatePokemon(pokemon: SavedPokemon) {
        savedPokeDao.updatePokemon(pokemon)
    }

    suspend fun insertPokemon(pokemon: SavedPokemon) {
        savedPokeDao.insertPokemon(pokemon)
    }

    suspend fun deletePokemon(pokemon: SavedPokemon) {
        savedPokeDao.deletePokemon(pokemon)
    }
}
