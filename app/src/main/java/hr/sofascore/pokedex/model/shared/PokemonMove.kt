package hr.sofascore.pokedex.model.shared

import hr.sofascore.pokedex.R
import java.io.Serializable

data class PokemonMove (
    val generation: Generation,
    val name: String,
    val damage_class: Result,
    val power: Int,
    val pp: Int
): Serializable

data class Generation (
    val name: String,
    val url: String
): Serializable {

    fun getRomanNumberGen(): String {
        val parts = name.split("-")
        return parts[parts.lastIndex].toUpperCase()
    }

    fun getColor(): Int {
        return when(name) {
            "generation-i" -> R.color.flat_pokemon_type_grass
            "generation-ii" -> R.color.flat_pokemon_type_bug
            "generation-iii" -> R.color.flat_pokemon_type_undefined
            "generation-iv" -> R.color.flat_pokemon_type_ghost
            "generation-v" -> R.color.flat_pokemon_type_water
            "generation-vi" -> R.color.flat_pokemon_type_fighting
            "generation-vii" -> R.color.flat_pokemon_type_fire
            "generation-viii" -> R.color.flat_pokemon_type_poison
            else -> R.color.surface_1
        }
    }
}