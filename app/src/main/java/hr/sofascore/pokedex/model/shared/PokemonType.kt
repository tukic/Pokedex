package hr.sofascore.pokedex.model.shared

import java.io.Serializable

data class PokemonType (
    val pokemon: List<PokemonTypeDetail>
): Serializable

data class PokemonTypeDetail (
    val pokemon: Result
)