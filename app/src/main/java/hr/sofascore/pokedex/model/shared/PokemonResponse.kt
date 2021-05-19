package hr.sofascore.pokedex.model.shared

import java.io.Serializable

data class PokemonResponse (
    val id: Int,
    val name: String
): Serializable