package com.example.pokedex.model.data.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.pokedex.model.entity.SavedPokemon

@Dao
interface SavedPokeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemon(pokemon: SavedPokemon)

    @Query("SELECT * FROM SavedPokemon")
    fun getPokemons(): LiveData<List<SavedPokemon>?>

    @Query("SELECT * FROM SavedPokemon WHERE name LIKE :name AND caught = 1 LIMIT 1")
    fun getPokemon(name: String): LiveData<SavedPokemon?>

    @Update
    suspend fun updatePokemon(pokemon: SavedPokemon):Int

    @Delete
    suspend fun deletePokemon(pokemon: SavedPokemon)
}
