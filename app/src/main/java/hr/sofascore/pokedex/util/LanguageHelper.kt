package hr.sofascore.pokedex.util

import android.content.Context
import android.preference.PreferenceManager

const val PREF_LANGUAGE_CODE = "PREF_LANGUAGE_CODE"
const val LOCALE_DEFAULT = "en"

object LanguageHelper {

    @JvmStatic
    fun setPreferredLanguage(context: Context, newLocale: String) =
        PreferenceManager.getDefaultSharedPreferences(context).edit()
            .putString(PREF_LANGUAGE_CODE, newLocale).apply()

    @JvmStatic
    fun getPreferredLanguage(context: Context) =
        PreferenceManager.getDefaultSharedPreferences(context)
            .getString(PREF_LANGUAGE_CODE, LOCALE_DEFAULT) ?: LOCALE_DEFAULT
}