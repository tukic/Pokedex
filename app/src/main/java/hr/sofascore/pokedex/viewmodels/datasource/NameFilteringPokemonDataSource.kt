package hr.sofascore.pokedex.viewmodels.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import hr.sofascore.pokedex.model.networking.Network
import hr.sofascore.pokedex.model.shared.PokemonResponse
import hr.sofascore.pokedex.viewmodels.pageSize
import kotlinx.coroutines.*

class NameFilteringPokemonDataSource(
    private val scope: CoroutineScope,
    private val filter: String,
    private val error: MutableLiveData<String>,
) :
    PageKeyedDataSource<Int, PokemonResponse>() {

    private val service = Network().getService()

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
            if(filteredResults?.isEmpty() == true) {
                callback.onResult(
                    arrayListOf(),
                    null,
                    null
                )
                return@launch
            }
            filteredResults?.subList(0, if(pageSize < filteredResults.size) pageSize else filteredResults.size-1)
            val async = filteredResults?.map {
                async {
                    service.getPokemonByURL(it.url).body()
                }
            }
            async?.awaitAll()?.let {
                callback.onResult(
                    it.filterNotNull(),
                    null,
                    if(counter >= pageSize) lastIndex else null
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
            if(filteredResults?.isEmpty() == true) {
                callback.onResult(
                    arrayListOf(),
                    null
                )
                return@launch
            }
            filteredResults?.subList(0, if(pageSize < filteredResults.size) pageSize else filteredResults.size-1)
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
            if(filteredResults?.isEmpty() == true) {
                callback.onResult(
                    arrayListOf(),
                    null,
                )
                return@launch
            }
            filteredResults?.subList(0, if(pageSize < filteredResults.size) pageSize else filteredResults.size-1)
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

    private fun handleError(exception: Throwable) {
        error.value = exception.toString()
    }
}