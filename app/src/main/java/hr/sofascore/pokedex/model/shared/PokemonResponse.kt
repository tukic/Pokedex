package hr.sofascore.pokedex.model.shared

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class PokemonResponse(
    @PrimaryKey
    val id: Int,
    val name: String,
) : Serializable

data class PokemonList(
    val count: Int,
    val next: String,
    val previous: String,
    val results: List<Result>
) : Serializable

data class Result(
    val name: String,
    val url: String
) : Serializable

