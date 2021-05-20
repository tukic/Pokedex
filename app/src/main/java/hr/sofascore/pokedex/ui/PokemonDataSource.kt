package hr.sofascore.pokedex.ui

import androidx.paging.PageKeyedDataSource
import hr.sofascore.pokedex.model.networking.Network
import hr.sofascore.pokedex.model.shared.PokemonResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class PokemonDataSource(private val initialURL: String, private val scope: CoroutineScope):
    PageKeyedDataSource<String, PokemonResponse>() {

    val service = Network().getService()

    override fun loadInitial(
        params: LoadInitialParams<String>,
        callback: LoadInitialCallback<String, PokemonResponse>
    ) {
        scope.launch {
            val pokemonUrlResponse = service.getPagedPokemon(initialURL)
            val pokemon = arrayListOf<PokemonResponse>()
            async {
                pokemonUrlResponse.body()?.results?.forEach {
                    service.getPokemonByURL(it.url).body()?.let {
                        pokemon.add(it)
                    }
                }
            }.await()
            callback.onResult(pokemon, null, pokemonUrlResponse.body()?.next)
        }
    }

    override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<String, PokemonResponse>) {
        scope.launch {
            val pokemonUrlResponse = service.getPagedPokemon(params.key)
            val pokemon = arrayListOf<PokemonResponse>()
            async {
                pokemonUrlResponse.body()?.results?.forEach {
                    service.getPokemonByURL(it.url).body()?.let {
                        pokemon.add(it)
                    }
                }
            }.await()
            val response = service.getPagedPokemon(params.key)
            callback.onResult(pokemon, response.body()?.next)
        }
    }

    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<String, PokemonResponse>) {
        scope.launch {
            val pokemonUrlResponse = service.getPagedPokemon(params.key)
            val pokemon = arrayListOf<PokemonResponse>()
            async {
                pokemonUrlResponse.body()?.results?.forEach {
                    service.getPokemonByURL(it.url).body()?.let {
                        pokemon.add(it)
                    }
                }
            }.await()
            val response = service.getPagedPokemon(params.key)
            callback.onResult(pokemon, response.body()?.next)
        }
    }

}