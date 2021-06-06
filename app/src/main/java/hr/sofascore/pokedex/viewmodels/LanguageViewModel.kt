package hr.sofascore.pokedex.viewmodels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.sofascore.pokedex.model.networking.Network
import hr.sofascore.pokedex.model.shared.Language
import hr.sofascore.pokedex.model.shared.PokeathlonStat
import hr.sofascore.pokedex.model.shared.Stat
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class LanguageViewModel : ViewModel() {
    val languages = MutableLiveData<Language>()
    val abilityTranslation = MutableLiveData<String>()
    val hiddenAbilityTranslation = MutableLiveData<String>()
    val statsTranslations = MutableLiveData<List<Stat>>()
    val pokeathlonStatsTranslations = MutableLiveData<List<PokeathlonStat>>()

    val error = MutableLiveData<String>()

    fun getLanguages() {
        val handler = CoroutineExceptionHandler { context, exception ->
            handleError(exception)
        }
        viewModelScope.launch(handler) {
                languages.value = Network().getService().getLanguages().body()
        }
    }

    fun translateAbility(context: Context, url: String) {
        val handler = CoroutineExceptionHandler { context, exception ->
            handleError(exception)
        }
        viewModelScope.launch(handler) {
                abilityTranslation.value =
                    Network().getService().getAbilityByURL(url).body()?.getName(context)

        }
    }

    fun translateHiddenAbility(context: Context, url: String) {
        val handler = CoroutineExceptionHandler { context, exception ->
            handleError(exception)
        }
        viewModelScope.launch(handler) {
            hiddenAbilityTranslation.value =
                    Network().getService().getAbilityByURL(url).body()?.getName(context)
        }
    }

    fun translateStats(urls: List<String>) {
        val handler = CoroutineExceptionHandler { context, exception ->
            handleError(exception)
        }
        viewModelScope.launch(handler) {
            val async = urls.map { url ->
                async {
                    Network().getService().getStatByURL(url).body()
                }
            }
            statsTranslations.value = async.awaitAll().filterNotNull()
        }
    }

    fun translatePokeatlhonStats(urls: List<String>) {
        val handler = CoroutineExceptionHandler { context, exception ->
            handleError(exception)
        }
        viewModelScope.launch(handler) {
            val async = urls.map { url ->
                async {
                    Network().getService().getPokeathlonStatByURL(url).body()
                }
            }
            try {
                pokeathlonStatsTranslations.value = async.awaitAll().filterNotNull()
            } catch (exception: Throwable) {
                error.value = exception.toString()
            }
        }
    }

    private fun handleError(exception: Throwable) {
        error.value = exception.toString()
    }
}