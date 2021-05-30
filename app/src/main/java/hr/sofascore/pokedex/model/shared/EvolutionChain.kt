package hr.sofascore.pokedex.model.shared

import java.io.Serializable

data class EvolutionChain (
    val chain: EvolutionChainDetail
): Serializable

data class EvolutionChainDetail (
    val evolves_to: List<EvolutionDescription>,
    val species: PokemonSpeciesDescription
): Serializable

data class EvolutionDescription (
    val species: PokemonSpeciesDescription,
    val evolves_to: List<EvolutionDescription>?,
    val evolution_details: List<EvolutionDetail>?
): Serializable

data class PokemonSpeciesDescription (
    val name: String,
    val url: String
): Serializable

data class EvolutionHelper (
    val name: String,
    var evolves_to: EvolutionHelper? = null,
    var pokemonResponse: PokemonResponse? = null,
    val minLevel: Int? = null
): Serializable {
    override fun equals(other: Any?): Boolean {
        if (other is EvolutionHelper) {
            return name == other.name
        }
        return false
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }

    fun getAllPokemonNames(): List<String>{
        val names = arrayListOf<String>()
        var current: EvolutionHelper? = this
        while (current != null) {
            names.add(current.name)
            current = current.evolves_to
        }
        return names
    }
}

data class EvolutionDetail(
    val min_level: Int
): Serializable
