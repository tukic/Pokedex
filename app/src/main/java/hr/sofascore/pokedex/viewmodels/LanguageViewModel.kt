package hr.sofascore.pokedex.viewmodels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.sofascore.pokedex.model.networking.Network
import hr.sofascore.pokedex.model.shared.Language
import hr.sofascore.pokedex.model.shared.PokeathlonStat
import hr.sofascore.pokedex.model.shared.Stat
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class LanguageViewModel : ViewModel() {
    val languages = MutableLiveData<Language>()
    val abilityTranslation = MutableLiveData<String>()
    val hiddenAbilityTranslation = MutableLiveData<String>()
    val statsTranslations = MutableLiveData<List<Stat>>()
    val pokeathlonStatsTranslations = MutableLiveData<List<PokeathlonStat>>()

    fun getLanguages() {
        viewModelScope.launch {
            languages.value = Network().getService().getLanguages().body()
        }
    }

    fun translateAbility(context: Context, url: String) {
        viewModelScope.launch {
            abilityTranslation.value =
                Network().getService().getAbilityByURL(url).body()?.getName(context)
        }
    }

    fun translateHiddenAbility(context: Context, url: String) {
        viewModelScope.launch {
            hiddenAbilityTranslation.value =
                Network().getService().getAbilityByURL(url).body()?.getName(context)
        }
    }

    fun translateStats(urls: List<String>) {
        viewModelScope.launch {
            val async = urls.map { url ->
                async {
                    Network().getService().getStatByURL(url).body()
                }
            }
            statsTranslations.value = async.awaitAll().filterNotNull()
        }
    }

    fun translatePokeatlhonStats(urls: List<String>) {
        viewModelScope.launch {
            val async = urls.map { url ->
                async {
                    Network().getService().getPokeathlonStatByURL(url).body()
                }
            }
            pokeathlonStatsTranslations.value = async.awaitAll().filterNotNull()
        }
    }
}