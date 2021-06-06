package hr.sofascore.pokedex.ui

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import hr.sofascore.pokedex.model.networking.Network
import hr.sofascore.pokedex.model.shared.PokemonResponse
import kotlinx.coroutines.*

class TypeFilteringPokemonDataSource(
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
            val typeUrlResponse =
                service.getPagedTypes(Int.MAX_VALUE).body()
            var lastIndex = 0
            val filteredResults = typeUrlResponse?.results?.filter { result ->
                result.name.contains(filter, true)
            }
            val async = filteredResults?.map {
                async {
                    service.getTypesByURL(it.url).body()
                }
            }
            val types = async?.awaitAll()

            val pokemonUrls = arrayListOf<String>()
            types?.forEach {
                it?.pokemon?.forEach {
                    pokemonUrls.add(it.pokemon.url)
                }
            }

            lastIndex = if(pokemonUrls.size > pageSize) pageSize
            else pokemonUrls.size-1

            if(pokemonUrls.isEmpty()) {
                callback.onResult(
                    arrayListOf(),
                    null,
                    null
                )
                return@launch
            }
            val pokemonOnePageFilteredUrls = pokemonUrls.subList(0, if(lastIndex > pokemonUrls.size - 1) pokemonUrls.size - 1 else lastIndex)

            val pokemons = pokemonOnePageFilteredUrls.map {
                async {
                    service.getPokemonByURL(it).body()
                }
            }

            pokemons?.awaitAll()?.let {
                callback.onResult(
                    it.filterNotNull().sortedBy { it.id },
                    null,
                    if(lastIndex >= pageSize) lastIndex else null
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
            val start = params.key
            val typeUrlResponse =
                service.getPagedTypes(Int.MAX_VALUE).body()
            var lastIndex = 0
            val filteredResults = typeUrlResponse?.results?.filter { result ->
                result.name.contains(filter, true)
            }
            val async = filteredResults?.map {
                async {
                    service.getTypesByURL(it.url).body()
                }
            }
            val types = async?.awaitAll()

            val pokemonUrls = arrayListOf<String>()
            types?.forEach {
                it?.pokemon?.forEach {
                    pokemonUrls.add(it.pokemon.url)
                }
            }

            lastIndex = if(pokemonUrls.size - 1 - start > pageSize) pageSize
            else pokemonUrls.size - 1 - start

            if(pokemonUrls.isEmpty()) {
                callback.onResult(
                    arrayListOf(),
                    null,
                )
                return@launch
            }
            val pokemonOnePageFilteredUrls = pokemonUrls.subList(start, if(start + lastIndex > pokemonUrls.size - 1) pokemonUrls.size - 1 else start + lastIndex)

            val pokemons = pokemonOnePageFilteredUrls.map {
                async {
                    service.getPokemonByURL(it).body()
                }
            }

            pokemons?.awaitAll()?.let {
                callback.onResult(
                    it.filterNotNull().sortedBy { it.id },
                    if(lastIndex >= pageSize) start + lastIndex else null
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
            val start = params.key
            val typeUrlResponse =
                service.getPagedTypes(Int.MAX_VALUE).body()
            var lastIndex = 0
            val filteredResults = typeUrlResponse?.results?.filter { result ->
                result.name.contains(filter, true)
            }
            val async = filteredResults?.map {
                async {
                    service.getTypesByURL(it.url).body()
                }
            }
            val types = async?.awaitAll()

            val pokemonUrls = arrayListOf<String>()
            types?.forEach {
                it?.pokemon?.forEach {
                    pokemonUrls.add(it.pokemon.url)
                }
            }

            lastIndex = if(pokemonUrls.size - 1 - start> pageSize) pageSize
            else pokemonUrls.size - 1 - start

            if(pokemonUrls.isEmpty()) {
                callback.onResult(
                    arrayListOf(),
                    null,
                )
                return@launch
            }
            val pokemonOnePageFilteredUrls = pokemonUrls.subList(start, if(start + lastIndex > pokemonUrls.size - 1) pokemonUrls.size - 1 else start + lastIndex)

            val pokemons = pokemonOnePageFilteredUrls.map {
                async {
                    service.getPokemonByURL(it).body()
                }
            }

            pokemons?.awaitAll()?.let {
                callback.onResult(
                    it.filterNotNull().sortedBy { it.id },
                    if(lastIndex >= pageSize) start + lastIndex else null
                )
            }
        }
    }

    private fun handleError(exception: Throwable) {
        error.value = exception.toString()
    }
}