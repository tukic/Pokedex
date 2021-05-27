package hr.sofascore.pokedex.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.sofascore.pokedex.model.networking.Network
import hr.sofascore.pokedex.model.shared.Language
import kotlinx.coroutines.launch

class LanguageViewModel: ViewModel() {
    val languages = MutableLiveData<Language>()

    fun getLanguages() {
        viewModelScope.launch {
            languages.value = Network().getService().getLanguages().body()
        }
    }
}