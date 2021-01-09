package com.example.pokedex.model.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.pokedex.model.data.DateConverter
import com.example.pokedex.model.entity.SavedPokemon

@Database(entities = [SavedPokemon::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class SavedPokemonRoomDatabase : RoomDatabase() {

    abstract fun pokeDao(): SavedPokeDao

    companion object {
        private const val DATABASE_NAME = "POKE_DATABASE"

        @Volatile
        private var INSTANCE: SavedPokemonRoomDatabase? = null

        fun getDatabase(context: Context): SavedPokemonRoomDatabase? {
            if (INSTANCE == null) {
                synchronized(SavedPokemonRoomDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            SavedPokemonRoomDatabase::class.java, DATABASE_NAME
                        )
                            .fallbackToDestructiveMigration()
                            .build()
                    }
                }
            }
            return INSTANCE
        }
    }

}
