package hr.sofascore.pokedex.model.shared

import java.io.Serializable

data class PokemonResponse (
    val id: Int,
    val name: String
): Serializable

data class PokemonList (
    val count: Int,
    val next: String,
    val previous: String,
    val results: List<Result>
): Serializable

data class Result (
    val name: String,
    val url: String
): Serializable

