package hr.sofascore.pokedex.model.shared

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
}