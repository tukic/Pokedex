package hr.sofascore.pokedex.model.db

import androidx.room.*
import hr.sofascore.pokedex.model.shared.PokemonResponse

@Dao
interface PokemonDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemon(pokemon: PokemonResponse)

    @Query("SELECT * FROM PokemonResponse WHERE id = :id")
    suspend fun getPokemonById(id: Int): List<PokemonResponse>

    @Query("SELECT * FROM PokemonResponse")
    suspend fun getAllPokemon(): List<PokemonResponse>

    @Delete
    suspend fun deletePokemon(pokemon: PokemonResponse)

}