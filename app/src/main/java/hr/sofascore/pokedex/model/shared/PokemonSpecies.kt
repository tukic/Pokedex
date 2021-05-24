package hr.sofascore.pokedex.model.shared

import java.io.Serializable

data class PokemonSpecies (
    val evolution_chain: EvolutionChainDescription
): Serializable

data class EvolutionChainDescription (
    val url: String
): Serializable