package hr.sofascore.pokedex.model.shared

import java.io.Serializable

data class EvolutionChain (
    val chain: EvolutionChainDetail
): Serializable

data class EvolutionChainDetail (
    val evolves_to: List<EvolutionDescription>
): Serializable

data class EvolutionDescription (
    val species: PokemonSpeciesDescription,
    val evolves_to: List<EvolutionDescription>?
): Serializable

data class PokemonSpeciesDescription (
    val name: String
): Serializable
