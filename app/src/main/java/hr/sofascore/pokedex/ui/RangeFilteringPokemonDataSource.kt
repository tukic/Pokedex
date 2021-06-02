package hr.sofascore.pokedex.ui

import androidx.paging.PageKeyedDataSource
import hr.sofascore.pokedex.model.networking.Network
import hr.sofascore.pokedex.model.shared.PokemonList
import hr.sofascore.pokedex.model.shared.PokemonResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class RangeFilteringPokemonDataSource(
    private val scope: CoroutineScope,
    private val start: Int,
    private val end: Int
) :
    PageKeyedDataSource<Int, PokemonResponse>() {

    val service = Network().getService()

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, PokemonResponse>
    ) {
        scope.launch {
            val limit = if (end - start < 20) end - start else 20
            val pokemonUrlResponse = service.getPagedPokemonByURL(start, limit)
            val async = pokemonUrlResponse.body()?.results?.map {
                async {
                    service.getPokemonByURL(it.url).body()
                }
            }
            async?.awaitAll()?.let {
                callback.onResult(
                    it.filterNotNull(),
                    null,
                    if (start + limit < end) start + limit else null
                )
            }
        }
    }

    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, PokemonResponse>
    ) {
        scope.launch {
            val newStart = params.key
            val limit = if (end - newStart < 20) end - newStart else 20
            val pokemonUrlResponse = service.getPagedPokemonByURL(newStart, limit)
            val async = pokemonUrlResponse.body()?.results?.map {
                async {
                    service.getPokemonByURL(it.url).body()
                }
            }
            async?.awaitAll()?.let {
                callback.onResult(
                    it.filterNotNull(),
                    if (newStart + limit < end) newStart + limit else null
                )
            }
        }
    }

    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, PokemonResponse>
    ) {
        scope.launch {
            val newStart = params.key
            val limit = if (end - newStart < 20) end - newStart else 20
            val pokemonUrlResponse = service.getPagedPokemonByURL(newStart, limit)
            val async = pokemonUrlResponse.body()?.results?.map {
                async {
                    service.getPokemonByURL(it.url).body()
                }
            }
            async?.awaitAll()?.let {
                callback.onResult(
                    it.filterNotNull(),
                    if (newStart + limit < end) newStart + limit else null
                )
            }
        }
    }
}