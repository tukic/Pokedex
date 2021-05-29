package hr.sofascore.pokedex.ui

import androidx.paging.PageKeyedDataSource
import hr.sofascore.pokedex.model.networking.Network
import hr.sofascore.pokedex.model.shared.PokemonList
import hr.sofascore.pokedex.model.shared.PokemonResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class PokemonDataSource(
    private val initialURL: String,
    private val scope: CoroutineScope,
) :
    PageKeyedDataSource<String, PokemonResponse>() {

    val service = Network().getService()

    override fun loadInitial(
        params: LoadInitialParams<String>,
        callback: LoadInitialCallback<String, PokemonResponse>
    ) {
        scope.launch {
            val pokemonUrlResponse = service.getPagedPokemonByURL(initialURL)
            val async = pokemonUrlResponse.body()?.results?.map {
                async {
                    service.getPokemonByURL(it.url).body()
                }
            }
            async?.awaitAll()?.let {
                callback.onResult(it.filterNotNull(), null, pokemonUrlResponse.body()?.next)
            }

        }
    }

    override fun loadBefore(
        params: LoadParams<String>,
        callback: LoadCallback<String, PokemonResponse>
    ) {
        scope.launch {
            val pokemonUrlResponse = service.getPagedPokemonByURL(params.key)
            val async = pokemonUrlResponse.body()?.results?.map {
                async {
                    service.getPokemonByURL(it.url).body()
                }
            }
            val response = service.getPagedPokemonByURL(params.key)
            async?.awaitAll()?.let {
                callback.onResult(it.filterNotNull(), response.body()?.next)
            }
        }
    }

    override fun loadAfter(
        params: LoadParams<String>,
        callback: LoadCallback<String, PokemonResponse>
    ) {
        scope.launch {
            val pokemonUrlResponse = service.getPagedPokemonByURL(params.key)
            val async = pokemonUrlResponse.body()?.results?.map {
                async {
                    service.getPokemonByURL(it.url).body()
                }
            }
            val response = service.getPagedPokemonByURL(params.key)
            async?.awaitAll()?.let {
                callback.onResult(it.filterNotNull(), response.body()?.next)
            }
        }
    }

}