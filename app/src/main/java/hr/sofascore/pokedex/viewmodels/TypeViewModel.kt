package hr.sofascore.pokedex.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.sofascore.pokedex.model.networking.Network
import hr.sofascore.pokedex.model.shared.PokemonResponse
import hr.sofascore.pokedex.model.shared.PokemonType
import hr.sofascore.pokedex.model.shared.PokemonTypeDescription
import hr.sofascore.pokedex.model.shared.PokemonTypeDetail
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class TypeViewModel : ViewModel() {
    val pokemons = MutableLiveData<List<PokemonResponse>>()
    val pokemonType = MutableLiveData<PokemonType>()
    val pokemonTypes = MutableLiveData<List<PokemonType>>()
    val pokemonTypesByUrls = MutableLiveData<List<PokemonType>>()

    fun getPokemons(pokemonList: List<PokemonTypeDetail>) {
        viewModelScope.launch {
            val tmp = arrayListOf<PokemonResponse>()
            async {
                pokemonList.forEach {
                    Network().getService().getPokemonByURL(it.pokemon.url).body()?.let {
                        tmp.add(it)
                    }
                }
            }.invokeOnCompletion {
                pokemons.value = tmp
            }
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
            val tmp = arrayListOf<PokemonType>()
            async {
                pokemon.types?.forEach {
                    Network().getService().getPokemonType(it.type.url).body()?.let {
                        tmp.add(it)
                    }
                }
            }.invokeOnCompletion {
                pokemonTypes.value = tmp
            }
        }
    }

    fun getTypesByUrls(urls: List<String>) {
        viewModelScope.launch {
            val tmp = arrayListOf<PokemonType>()
            async {
                urls.forEach {
                    Network().getService().getPokemonType(it).body()?.let {
                        tmp.add(it)
                    }
                }
            }.invokeOnCompletion {
                pokemonTypesByUrls.value = tmp
            }
        }
    }
}