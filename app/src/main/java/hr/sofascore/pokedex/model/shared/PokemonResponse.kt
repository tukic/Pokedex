package hr.sofascore.pokedex.model.shared

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class PokemonResponse @JvmOverloads constructor (
    @PrimaryKey
    val id: Int,
    val name: String,
    @Ignore
    var types: List<PokemonType>? = arrayListOf()
) : Serializable {

    fun getFormattedId() = "%03d".format(id)
    fun getImageURL() = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${id}.png"
}

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

data class PokemonType (
    val slot: Int,
    val type: PokemonTypeDescription
): Serializable

data class PokemonTypeDescription (
    val name: String,
    val url: String
): Serializable

