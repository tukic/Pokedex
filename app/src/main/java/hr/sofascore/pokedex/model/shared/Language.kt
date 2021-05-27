package hr.sofascore.pokedex.model.shared

import java.io.Serializable

data class Language (
    val count: Int,
    val results: List<Result>
): Serializable