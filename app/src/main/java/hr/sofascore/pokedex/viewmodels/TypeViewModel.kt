package hr.sofascore.pokedex.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.sofascore.pokedex.model.networking.Network
import hr.sofascore.pokedex.model.shared.PokemonResponse
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class TypeViewModel : ViewModel() {
    val pokemons = MutableLiveData<List<PokemonResponse>>()

    fun getPokemons(url: String) {
        viewModelScope.launch {
            Network().getService().getPokemonsFromType(url).body()?.let {
                val tmp = arrayListOf<PokemonResponse>()
                async {
                    it.pokemon.forEach {
                        Network().getService().getPokemonByURL(it.pokemon.url).body()?.let {
                            tmp.add(it)
                        }
                    }
                }.invokeOnCompletion {
                    pokemons.value = tmp
                }
            }
        }
    }
}