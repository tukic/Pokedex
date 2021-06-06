package hr.sofascore.pokedex.viewmodels.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import hr.sofascore.pokedex.model.networking.Network
import hr.sofascore.pokedex.model.shared.PokemonResponse
import kotlinx.coroutines.*

class RangeFilteringPokemonDataSource(
    private val scope: CoroutineScope,
    private val start: Int,
    private val end: Int,
    private val error: MutableLiveData<String>
) :
    PageKeyedDataSource<Int, PokemonResponse>() {

    val service = Network().getService()

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, PokemonResponse>
    ) {
        val handler = CoroutineExceptionHandler { context, exception ->
            handleError(exception)
        }
        scope.launch(handler) {
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
        val handler = CoroutineExceptionHandler { context, exception ->
            handleError(exception)
        }
        scope.launch(handler) {
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
        val handler = CoroutineExceptionHandler { context, exception ->
            handleError(exception)
        }
        scope.launch(handler) {
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

    private fun handleError(exception: Throwable) {
        error.value = exception.toString()
    }
}