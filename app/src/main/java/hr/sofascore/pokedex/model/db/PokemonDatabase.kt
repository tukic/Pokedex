package hr.sofascore.pokedex.model.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import hr.sofascore.pokedex.model.shared.PokemonResponse

@Database(entities = [PokemonResponse::class], version = 1, exportSchema = false)
abstract class PokemonDatabase: RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao

    companion object {
        private var instance: PokemonDatabase? = null

        fun getDatabase(context: Context): PokemonDatabase? {
            if(instance == null) {
                instance = buildDatabase(context)
            }
            return instance
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, PokemonDatabase::class.java, "PokemonDatabase.db").build()
    }
}