package hr.sofascore.pokedex.ui

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import hr.sofascore.pokedex.model.networking.Network
import hr.sofascore.pokedex.model.shared.PokemonResponse
import kotlinx.coroutines.*

class NameFilteringPokemonDataSource(
    private val scope: CoroutineScope,
    private val filter: String,
    private val error: MutableLiveData<String>,
    private val pageSize: Int = 10
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
            val pokemonUrlResponse =
                service.getPagedPokemons(Int.MAX_VALUE).body()
            val count = pokemonUrlResponse!!.count
            var counter = 0
            var lastIndex = 0
            val filteredResults = pokemonUrlResponse?.results?.filterIndexed { index, result ->
                if(result.name.contains(filter, true) && counter < pageSize) {
                    counter++
                    lastIndex = index
                    true
                } else {
                    false
                }
            }
            if(filteredResults.isEmpty()) {
                callback.onResult(
                    arrayListOf(),
                    null,
                    if(counter >= pageSize) lastIndex else null
                )
                return@launch
            }
            filteredResults.subList(0, if(pageSize < filteredResults.size) pageSize else filteredResults.size-1)
            val async = filteredResults?.map {
                async {
                    service.getPokemonByURL(it.url).body()
                }
            }
            async?.awaitAll()?.let {
                callback.onResult(
                    it.filterNotNull(),
                    null,
                    null
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
            val offset = params.key
            val pokemonUrlResponse =
                service.getPagedPokemonByURL(offset, Int.MAX_VALUE).body()
            val count = pokemonUrlResponse!!.count
            var counter = 0
            var lastIndex = 0
            val filteredResults = pokemonUrlResponse?.results?.filterIndexed { index, result ->
                if(result.name.contains(filter, true) && counter < pageSize) {
                    counter++
                    lastIndex = index
                    true
                } else {
                    false
                }
            }
            if(filteredResults.isEmpty()) {
                callback.onResult(
                    arrayListOf(),
                    null
                )
                return@launch
            }
            filteredResults.subList(0, if(pageSize < filteredResults.size) pageSize else filteredResults.size-1)
            lastIndex += offset
            val async = filteredResults?.map {
                async {
                    service.getPokemonByURL(it.url).body()
                }
            }
            async?.awaitAll()?.let {
                callback.onResult(
                    it.filterNotNull(),
                    if(counter >= pageSize) lastIndex else null
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
            val offset = params.key
            val pokemonUrlResponse =
                service.getPagedPokemonByURL(offset, Int.MAX_VALUE).body()
            val count = pokemonUrlResponse!!.count
            var lastIndex = 0
            var counter = 0
            val filteredResults = pokemonUrlResponse?.results?.filterIndexed { index, result ->
                if(result.name.contains(filter, true) && counter < pageSize) {
                    counter++
                    lastIndex = index
                    true
                } else {
                    false
                }
            }
            if(filteredResults.isEmpty()) {
                callback.onResult(
                    arrayListOf(),
                    null,
                )
                return@launch
            }
            filteredResults.subList(0, if(pageSize < filteredResults.size) pageSize else filteredResults.size-1)
            lastIndex += offset
            val async = filteredResults?.map {
                async {
                    service.getPokemonByURL(it.url).body()
                }
            }
            async?.awaitAll()?.let {
                callback.onResult(
                    it.filterNotNull(),
                    //if (lastIndex - 1 < count) lastIndex else null
                    if(counter >= pageSize) lastIndex else null
                )
            }
        }
    }

    private fun handleError(exception: Throwable) {
        error.value = exception.toString()
    }
}