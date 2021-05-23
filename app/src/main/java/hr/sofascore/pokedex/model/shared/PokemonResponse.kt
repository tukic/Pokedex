package hr.sofascore.pokedex.model.shared

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import hr.sofascore.pokedex.R
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
): Serializable {
    fun getTypeColor(): Int {
        when(name) {
            "bug" -> return R.color.flat_pokemon_type_bug
            "dark" -> return R.color.flat_pokemon_type_dark
            "dragon" -> return R.color.flat_pokemon_type_dragon
            "electric" -> return R.color.flat_pokemon_type_electric
            "fairy" -> return R.color.flat_pokemon_type_fairy
            "fighting" -> return R.color.flat_pokemon_type_fighting
            "fire" -> return R.color.flat_pokemon_type_fire
            "flying" -> return R.color.flat_pokemon_type_flying
            "ghost" -> return R.color.flat_pokemon_type_ghost
            "grass" -> return R.color.flat_pokemon_type_grass
            "ground" -> return R.color.flat_pokemon_type_ground
            "ice" -> return R.color.flat_pokemon_type_ice
            "normal" -> return R.color.flat_pokemon_type_normal
            "poison" -> return R.color.flat_pokemon_type_poison
            "psychic" -> return R.color.flat_pokemon_type_psychic
            "rock" -> return R.color.flat_pokemon_type_rock
            "steel" -> return R.color.flat_pokemon_type_steel
            "water" -> return R.color.flat_pokemon_type_water
        }
        return R.color.flat_pokemon_type_undefined
    }
}

