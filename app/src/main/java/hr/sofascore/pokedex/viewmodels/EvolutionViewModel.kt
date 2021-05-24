package hr.sofascore.pokedex.viewmodels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import hr.sofascore.pokedex.model.networking.Network
import hr.sofascore.pokedex.model.shared.EvolutionDescription
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class EvolutionViewModel : ViewModel() {
    val evolutions = MutableLiveData<List<EvolutionDescription>>()

    fun getEvolutions(context: Context, pokemonId: Int) {
        viewModelScope.launch {
            val tmp: ArrayList<EvolutionDescription> = arrayListOf()
            async {
                Network().getService().getSpecies(pokemonId).body()?.let {
                    Network().getService().getEvolutionChain(it.evolution_chain.url).body()?.let {
                        it.chain.evolves_to.forEach {
                            tmp.add(it)
                        }
                    }
                }
            }.invokeOnCompletion {
                evolutions.value = tmp
            }
        }
    }
}