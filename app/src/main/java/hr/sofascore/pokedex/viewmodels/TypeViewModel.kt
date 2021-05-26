package hr.sofascore.pokedex.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.sofascore.pokedex.model.networking.Network
import hr.sofascore.pokedex.model.shared.PokemonResponse
import hr.sofascore.pokedex.model.shared.PokemonType
import hr.sofascore.pokedex.model.shared.PokemonTypeDetail
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class TypeViewModel : ViewModel() {
    val pokemons = MutableLiveData<List<PokemonResponse>>()
    val pokemonType = MutableLiveData<PokemonType>()
    val pokemonTypes = MutableLiveData<List<PokemonType>>()
    val pokemonTypesByUrls = MutableLiveData<List<PokemonType>>()

    fun getPokemons(pokemonList: List<PokemonTypeDetail>) {
        viewModelScope.launch {
            val asyncTask = pokemonList.map {
                async {
                    Network().getService().getPokemonByURL(it.pokemon.url).body()
                }
            }
            pokemons.value = asyncTask.awaitAll().filterNotNull()
        }
    }


    fun getType(url: String) {
        viewModelScope.launch {
            Network().getService().getPokemonType(url).body()?.let {
                pokemonType.value = it
            }
        }
    }

    fun getPokemonTypes(pokemon: PokemonResponse) {
        viewModelScope.launch {
            pokemonTypes.value =
                pokemon.types?.map {
                    Network().getService().getPokemonType(it.type.url).body()
                }?.toList()?.filterNotNull()
        }
    }

    fun getTypesByUrls(urls: List<String>) {
        viewModelScope.launch {
            pokemonTypesByUrls.value =
                urls.map {
                    Network().getService().getPokemonType(it).body()
                }.toList().filterNotNull()
        }
    }
}