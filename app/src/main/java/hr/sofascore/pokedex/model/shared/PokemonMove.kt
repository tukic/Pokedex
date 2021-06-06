package hr.sofascore.pokedex.model.shared

import android.content.Context
import hr.sofascore.pokedex.R
import hr.sofascore.pokedex.util.LanguageHelper
import java.io.Serializable
import java.util.*

data class PokemonMove (
    val generation: Generation,
    val name: String,
    val damage_class: Result,
    var damage_class_detail: Translation?,
    val power: Int,
    val pp: Int,
    val names: List<LanguageDescription>
): Serializable {
    fun getName(context: Context): String {
        val language = LanguageHelper.getPreferredLanguage(context)
        return names.find { it.language.name == language }?.name ?: this.name
    }
}

data class Generation (
    val name: String,
    val url: String
): Serializable {

    fun getRomanNumberGen(): String {
        val parts = name.split("-")
        return parts[parts.lastIndex].toUpperCase(Locale.getDefault())
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