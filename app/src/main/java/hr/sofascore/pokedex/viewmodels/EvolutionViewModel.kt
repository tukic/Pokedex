package hr.sofascore.pokedex.viewmodels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import hr.sofascore.pokedex.model.networking.Network
import hr.sofascore.pokedex.model.shared.EvolutionChain
import hr.sofascore.pokedex.model.shared.EvolutionDescription
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class EvolutionViewModel : ViewModel() {
    val evolutions = MutableLiveData<List<EvolutionDescription>>()
    val evolution = MutableLiveData<EvolutionChain>()

    fun getEvolutions(pokemonId: Int) {
        viewModelScope.launch {
            evolutions.value =
                Network().getService().getSpecies(pokemonId).body()?.let {
                    Network().getService().getEvolutionChain(it.evolution_chain.url)
                        .body()?.chain?.evolves_to
                }
        }
    }

    fun getEvolution(pokemonId: Int) {
        viewModelScope.launch {
            evolution.value =
                Network().getService().getSpecies(pokemonId).body()?.let {
                    Network().getService().getEvolutionChain(it.evolution_chain.url).body()
                }
        }
    }
}