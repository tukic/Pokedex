package hr.sofascore.pokedex.model.shared

import android.content.Context
import hr.sofascore.pokedex.util.LanguageHelper
import java.io.Serializable

data class Language (
    val count: Int,
    val results: List<Result>
): Serializable

data class Translation(
    val names: List<LanguageDescription>,
    val name: String
) : Serializable {
    fun getName(context: Context): String {
        val language = LanguageHelper.getPreferredLanguage(context)
        return names.find { it.language.name == language }?.name ?: this.name
    }
}

data class LanguageDescription(
    val language: Result,
    val name: String
): Serializable