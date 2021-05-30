package hr.sofascore.pokedex.model.shared

import android.content.Context
import hr.sofascore.pokedex.util.LanguageHelper

data class PokemonDamageClass(
    val names: List<LanguageDescription>,
    val name: String
) {
    fun getName(context: Context): String {
        val language = LanguageHelper.getPreferredLanguage(context)
        return names.find { it.language.name == language }?.name ?: this.name
    }
}