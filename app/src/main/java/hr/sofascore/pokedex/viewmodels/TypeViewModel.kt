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

    val offensiveDoubleDamageTypes = MutableLiveData<List<PokemonType>>()
    val offensiveHalfDamageTypes = MutableLiveData<List<PokemonType>>()
    val offensiveNoDamageTypes = MutableLiveData<List<PokemonType>>()
    val defensiveHalfDamageTypes = MutableLiveData<List<PokemonType>>()
    val defensiveDoubleDamageTypes = MutableLiveData<List<PokemonType>>()
    val defensiveNoDamageTypes = MutableLiveData<List<PokemonType>>()


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

    fun getOffensiveDoubleDamageTypes(urls: List<String>) {
        viewModelScope.launch {
            offensiveDoubleDamageTypes.value =
                urls.map {
                    Network().getService().getPokemonType(it).body()
                }.toList().filterNotNull()
        }
    }

    fun getOffensiveHalfDamageTypes(urls: List<String>) {
        viewModelScope.launch {
            offensiveHalfDamageTypes.value =
                urls.map {
                    Network().getService().getPokemonType(it).body()
                }.toList().filterNotNull()
        }
    }

    fun getOffensiveNoDamageTypes(urls: List<String>) {
        viewModelScope.launch {
            offensiveNoDamageTypes.value =
                urls.map {
                    Network().getService().getPokemonType(it).body()
                }.toList().filterNotNull()
        }
    }

    fun getDefensiveHalfDamageTypes(urls: List<String>) {
        viewModelScope.launch {
            defensiveHalfDamageTypes.value =
                urls.map {
                    Network().getService().getPokemonType(it).body()
                }.toList().filterNotNull()
        }
    }

    fun getDefensiveDoubleTypes(urls: List<String>) {
        viewModelScope.launch {
            defensiveDoubleDamageTypes.value =
                urls.map {
                    Network().getService().getPokemonType(it).body()
                }.toList().filterNotNull()
        }
    }

    fun getDefensiveNoDamageTypes(urls: List<String>) {
        viewModelScope.launch {
            defensiveNoDamageTypes.value =
                urls.map {
                    Network().getService().getPokemonType(it).body()
                }.toList().filterNotNull()
        }
    }
}