package com.example.pokedex.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.pokedex.model.SavedPokemon

@Database(entities = [SavedPokemon::class], version = 1, exportSchema = false)
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
//                            .addCallback(object : RoomDatabase.Callback() {
//                                override fun onCreate(db: SupportSQLiteDatabase) {
//                                    super.onCreate(db)
//                                    INSTANCE?.let { database ->
//                                        CoroutineScope(Dispatchers.IO).launch {
//                                            database.gameDao().insertGame(Game(null,"Title", "Initial Text", Date()))
//                                        }
//                                    }
//                                }
//                            })
                            .build()
                    }
                }
            }
            return INSTANCE
        }
    }

}
