package hr.sofascore.pokedex.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.sofascore.pokedex.model.networking.Network
import hr.sofascore.pokedex.model.shared.PokemonResponse
import hr.sofascore.pokedex.model.shared.PokemonType
import hr.sofascore.pokedex.model.shared.PokemonTypeDetail
import kotlinx.coroutines.CoroutineExceptionHandler
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
    val error = MutableLiveData<String>()

    fun getPokemons(pokemonList: List<PokemonTypeDetail>) {
        val handler = CoroutineExceptionHandler { context, exception ->
            handleError(exception)
        }
        viewModelScope.launch(handler) {
            val asyncTask = pokemonList.map {
                async {
                    Network().getService().getPokemonByURL(it.pokemon.url).body()
                }
            }
            pokemons.value = asyncTask.awaitAll().filterNotNull()
        }
    }


    fun getType(url: String) {
        val handler = CoroutineExceptionHandler { context, exception ->
            handleError(exception)
        }
        viewModelScope.launch(handler) {
            Network().getService().getPokemonType(url).body()?.let {
                pokemonType.value = it
            }
        }
    }

    fun getPokemonTypes(pokemon: PokemonResponse) {
        val handler = CoroutineExceptionHandler { context, exception ->
            handleError(exception)
        }
        viewModelScope.launch(handler) {
            pokemonTypes.value =
                pokemon.types?.map {
                    val response = Network().getService().getPokemonType(it.type.url)
                    response.body()
                }?.toList()?.filterNotNull()
        }
    }

    fun getOffensiveDoubleDamageTypes(urls: List<String>) {
        val handler = CoroutineExceptionHandler { context, exception ->
            handleError(exception)
        }
        viewModelScope.launch(handler) {
            offensiveDoubleDamageTypes.value =
                urls.map {
                    Network().getService().getPokemonType(it).body()
                }.toList().filterNotNull()
        }
    }

    fun getOffensiveHalfDamageTypes(urls: List<String>) {
        val handler = CoroutineExceptionHandler { context, exception ->
            handleError(exception)
        }
        viewModelScope.launch(handler) {
            offensiveHalfDamageTypes.value =
                urls.map {
                    Network().getService().getPokemonType(it).body()
                }.toList().filterNotNull()
        }
    }

    fun getOffensiveNoDamageTypes(urls: List<String>) {
        val handler = CoroutineExceptionHandler { context, exception ->
            handleError(exception)
        }
        viewModelScope.launch(handler) {
            offensiveNoDamageTypes.value =
                urls.map {
                    Network().getService().getPokemonType(it).body()
                }.toList().filterNotNull()
        }
    }

    fun getDefensiveHalfDamageTypes(urls: List<String>) {
        val handler = CoroutineExceptionHandler { context, exception ->
            handleError(exception)
        }
        viewModelScope.launch(handler) {
            defensiveHalfDamageTypes.value =
                urls.map {
                    Network().getService().getPokemonType(it).body()
                }.toList().filterNotNull()
        }
    }

    fun getDefensiveDoubleTypes(urls: List<String>) {
        val handler = CoroutineExceptionHandler { context, exception ->
            handleError(exception)
        }
        viewModelScope.launch(handler) {
            defensiveDoubleDamageTypes.value =
                urls.map {
                    Network().getService().getPokemonType(it).body()
                }.toList().filterNotNull()
        }
    }

    fun getDefensiveNoDamageTypes(urls: List<String>) {
        val handler = CoroutineExceptionHandler { context, exception ->
            handleError(exception)
        }
        viewModelScope.launch(handler) {
            defensiveNoDamageTypes.value =
                urls.map {
                    Network().getService().getPokemonType(it).body()
                }.toList().filterNotNull()

        }
    }

    fun handleError(exception: Throwable) {
        error.value = exception.toString()
    }
}