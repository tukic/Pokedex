package hr.sofascore.pokedex.viewmodels

import android.content.Context
import androidx.lifecycle.*
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import hr.sofascore.pokedex.model.db.PokemonDatabase
import hr.sofascore.pokedex.model.networking.Network
import hr.sofascore.pokedex.model.networking.initialPokemonURL
import hr.sofascore.pokedex.model.shared.PokemonList
import hr.sofascore.pokedex.model.shared.PokemonResponse
import hr.sofascore.pokedex.model.shared.PokemonType
import hr.sofascore.pokedex.ui.NameFilteringPokemonDataSource
import hr.sofascore.pokedex.ui.PokemonDataSource
import hr.sofascore.pokedex.ui.RangeFilteringPokemonDataSource
import hr.sofascore.pokedex.ui.TypeFilteringPokemonDataSource
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import java.lang.Exception

class PokemonViewModel : ViewModel() {

    val pokemon = MutableLiveData<PokemonResponse>()
    val pokemonPagedList: LiveData<PagedList<PokemonResponse>>
    val pokemonNameFilteredPagedList: LiveData<PagedList<PokemonResponse>>
    val pokemonTypeFilteredPagedList: LiveData<PagedList<PokemonResponse>>
    val pokemonRangeFilteredPagedList: LiveData<PagedList<PokemonResponse>>

    val favouritePokemon = MutableLiveData<List<PokemonResponse>>()

    val allPokemons = MutableLiveData<PokemonList>()
    val filteredPokemons = MutableLiveData<List<PokemonResponse>>()

    val pokemonCount = MutableLiveData<Int>()

    val evolutionPokemons = MutableLiveData<List<PokemonResponse>>()

    val noFilter = MutableLiveData<Boolean>()
    val pokemonNameFilter = MutableLiveData<String>()
    val pokemonTypeFilter = MutableLiveData<String>()
    val rangeFilter = MutableLiveData<List<Int>>()

    val error = MutableLiveData<String>()

    init {
        evolutionPokemons.value = arrayListOf<PokemonResponse>()
        val config = PagedList.Config.Builder().setPageSize(10).setEnablePlaceholders(false).build()

        pokemonPagedList =
            noFilter.switchMap { input ->
                initializePagedList(config).build()
            }

        pokemonNameFilteredPagedList = pokemonNameFilter.switchMap { input ->
            if (input == null) {
                initializePagedList(config).build()
            }
            initializeNameFilteredPagedList(config, input).build()
        }

        pokemonTypeFilteredPagedList = pokemonTypeFilter.switchMap { input ->
            if (input == null) {
                initializePagedList(config).build()
            }
            initializeTypeFilteredPagedList(config, input).build()
        }

        pokemonRangeFilteredPagedList = rangeFilter.switchMap { input ->
            if (input == null) {
                initializePagedList(config).build()
            }
            initializeRangeFilteredPagedList(config, input[0], input[1]).build()
        }
    }

    private fun initializePagedList(config: PagedList.Config): LivePagedListBuilder<String, PokemonResponse> {
        val dataSource = object : DataSource.Factory<String, PokemonResponse>() {
            override fun create(): DataSource<String, PokemonResponse> {
                return PokemonDataSource(initialPokemonURL, viewModelScope, error)
            }
        }
        return LivePagedListBuilder(dataSource, config)
    }

    private fun initializeNameFilteredPagedList(
        config: PagedList.Config,
        filter: String
    ): LivePagedListBuilder<Int, PokemonResponse> {
        val dataSource = object : DataSource.Factory<Int, PokemonResponse>() {
            override fun create(): DataSource<Int, PokemonResponse> {
                return NameFilteringPokemonDataSource(viewModelScope, filter, error)
            }
        }
        return LivePagedListBuilder(dataSource, config)
    }

    private fun initializeTypeFilteredPagedList(
        config: PagedList.Config,
        filter: String
    ): LivePagedListBuilder<Int, PokemonResponse> {
        val dataSource = object : DataSource.Factory<Int, PokemonResponse>() {
            override fun create(): DataSource<Int, PokemonResponse> {
                return TypeFilteringPokemonDataSource(viewModelScope, filter, error)
            }
        }
        return LivePagedListBuilder(dataSource, config)
    }

    private fun initializeRangeFilteredPagedList(
        config: PagedList.Config,
        start: Int,
        end: Int
    ): LivePagedListBuilder<Int, PokemonResponse> {
        val dataSource = object : DataSource.Factory<Int, PokemonResponse>() {
            override fun create(): DataSource<Int, PokemonResponse> {
                return RangeFilteringPokemonDataSource(viewModelScope, start, end, error)
            }
        }
        return LivePagedListBuilder(dataSource, config)
    }

    fun getPokemon(name: String) {
        val handler = CoroutineExceptionHandler { context, exception ->
            handleError(exception)
        }
        viewModelScope.launch(handler) {
            try {
                pokemon.value = Network().getService().getPokemonByName(name).body()
            } catch (exception: Throwable) {
                error.value = exception.toString()
            }
        }
    }

    fun getPokemon(id: Int) {
        val handler = CoroutineExceptionHandler { context, exception ->
            handleError(exception)
        }
        viewModelScope.launch(handler) {
            try {
                pokemon.value = Network().getService().getPokemonById(id).body().apply {
                    this?.abilities?.forEach {
                        Network().getService().getAbilityByURL(it.ability.url).body()
                            ?.let { ability ->
                                this.ability?.add(
                                    ability
                                )
                            }
                    }
                    this?.stats?.forEach {
                        Network().getService().getStatByURL(it.stat.url).body()?.let { stat ->
                            this.stat?.add(
                                stat
                            )
                        }
                    }
                }
            } catch (exception: Throwable) {
                error.value = exception.toString()
            }
        }
    }

    fun insertFavouritePokemon(context: Context, pokemon: PokemonResponse) {
        val handler = CoroutineExceptionHandler { context, exception ->
            handleError(exception)
        }
        viewModelScope.launch(handler) {
            try {
                val index =
                    PokemonDatabase.getDatabase(context)?.pokemonDao()?.getMaxFavoriteIndex()
                if (index != null) {
                    pokemon.favoriteIndex = index + 1
                }
                PokemonDatabase.getDatabase(context)?.pokemonDao()?.insertPokemon(pokemon)
                getFavouritePokemonSortedByByFavoriteIndex(context)
            } catch (exception: Throwable) {
                error.value = exception.toString()
            }
        }
    }

    fun getFavouritePokemonSortedByByFavoriteIndex(context: Context) {
        val handler = CoroutineExceptionHandler { context, exception ->
            handleError(exception)
        }
        viewModelScope.launch(handler) {
            try {
                favouritePokemon.value =
                    PokemonDatabase.getDatabase(context)?.pokemonDao()
                        ?.getAllPokemonSortedByFavoriteIndex()
            } catch (exception: Throwable) {
                error.value = exception.toString()
            }
        }
    }

    fun deleteFavouritePokemon(context: Context, pokemon: PokemonResponse) {
        val handler = CoroutineExceptionHandler { context, exception ->
            handleError(exception)
        }
        viewModelScope.launch(handler) {
            try {
                PokemonDatabase.getDatabase(context)?.pokemonDao()?.deletePokemon(pokemon)
                getFavouritePokemonSortedByByFavoriteIndex(context)
            } catch (exception: Throwable) {
                error.value = exception.toString()
            }
        }
    }

    fun updatePokemon(context: Context, pokemons: List<PokemonResponse>) {
        val handler = CoroutineExceptionHandler { context, exception ->
            handleError(exception)
        }
        viewModelScope.launch(handler) {
            try {
                pokemons.forEach {
                    async {
                        PokemonDatabase.getDatabase(context)?.pokemonDao()?.updatePokemon(it)
                    }
                }
            } catch (exception: Throwable) {
                error.value = exception.toString()
            }
        }
    }

    fun deleteAllPokemons(context: Context) {
        val handler = CoroutineExceptionHandler { context, exception ->
            handleError(exception)
        }
        viewModelScope.launch(handler) {
            try {
                PokemonDatabase.getDatabase(context)?.pokemonDao()?.deleteAllPokemons()
            } catch (exception: Throwable) {
                error.value = exception.toString()
            }
        }
    }

    fun getAllPokemons() {
        val handler = CoroutineExceptionHandler { context, exception ->
            handleError(exception)
        }
        viewModelScope.launch(handler) {
            try {
                Network().getService().getPagedPokemons(Int.MAX_VALUE).body()?.let {
                    allPokemons.value = it
                }
            } catch (exception: Throwable) {
                error.value = exception.toString()
            }
        }
    }

    fun getPokemonsFilteredByName(name: String) {
        val handler = CoroutineExceptionHandler { context, exception ->
            handleError(exception)
        }
        viewModelScope.launch(handler) {
            try {
                Network().getService().getPagedPokemons(Int.MAX_VALUE).body()?.let {
                    val async = it.results.filter { it.name.contains(name) }.map {
                        async {
                            Network().getService().getPokemonByURL(it.url).body()
                        }
                    }
                    filteredPokemons.value = async.awaitAll().filterNotNull()
                }
            } catch (exception: Throwable) {
                error.value = exception.toString()
            }
        }
    }

    fun getPokemonsFilteredByRange(start: Int, end: Int) {
        val handler = CoroutineExceptionHandler { context, exception ->
            handleError(exception)
        }
        viewModelScope.launch(handler) {
            try {
                Network().getService().getPagedPokemons(Int.MAX_VALUE).body()?.let {
                    val async = it.results.filter {
                        val parts = it.url.split("/")
                        parts[parts.size - 2].toInt() in start..end
                    }.map {
                        async {
                            Network().getService().getPokemonByURL(it.url).body()
                        }
                    }
                    filteredPokemons.value = async.awaitAll().filterNotNull()
                }
            } catch (exception: Throwable) {
                error.value = exception.toString()
            }
        }
    }

    fun getPokemonsFilteredByType(type: String) {
        val handler = CoroutineExceptionHandler { context, exception ->
            handleError(exception)
        }
        viewModelScope.launch(handler) {
            try {
                Network().getService().getPagedTypes(Int.MAX_VALUE).body()?.let {
                    val asyncTypes = it.results.filter { it.name.contains(type) }.map {
                        async {
                            Network().getService().getTypesByURL(it.url).body()
                        }
                    }
                    val types = asyncTypes.awaitAll().filterNotNull()

                    val pokemonResults = types.flatMap { it.pokemon }

                    val asyncPokemons = pokemonResults.map {
                        async {
                            Network().getService().getPokemonByURL(it.pokemon.url).body()
                        }
                    }
                    filteredPokemons.value =
                        asyncPokemons.awaitAll().filterNotNull().sortedBy { it.id }
                }
            } catch (exception: Throwable) {
                error.value = exception.toString()
            }
        }
    }


    fun getPokemonCount() {
        val handler = CoroutineExceptionHandler { context, exception ->
            handleError(exception)
        }
        viewModelScope.launch(handler) {
            try {
                Network().getService().getPagedPokemons(Int.MAX_VALUE).body()?.let {
                    pokemonCount.value = it.count
                }
            } catch (exception: Throwable) {
                error.value = exception.toString()
            }
        }
    }

    /*
    fun getPokemonRangeFilteredPagedList(start: Int, end: Int) {
        val config = PagedList.Config.Builder().setPageSize(20).setEnablePlaceholders(false).build()
        pokemonRangeFilteredPagedList = initializeRangeFilteredPagedList(config, start, end).build()
    }

     */

    fun getEvolutionPokemon(names: List<String>) {
        val handler = CoroutineExceptionHandler { context, exception ->
            handleError(exception)
        }
        viewModelScope.launch(handler) {
            try {
                val tmp = ArrayList<PokemonResponse>()
                evolutionPokemons.value?.let {
                    tmp.addAll(it)
                }
                val async = names.map {
                    async {
                        Network().getService().getPokemonByName(it).body().apply {
                            val tmp = arrayListOf<PokemonType>()
                            this?.types?.forEach {
                                it.type.url?.let {
                                    Network().getService().getPokemonType(it).body()?.let {
                                        tmp.add(it)
                                    }
                                }
                                this.typeDetail = tmp
                            }
                        }
                    }
                }
                evolutionPokemons.value = async.awaitAll().filterNotNull()
            } catch (exception: Throwable) {
                error.value = exception.toString()
            }
        }
    }

    fun filterByPokemonName(filter: String) {
        pokemonNameFilter.value = filter
    }

    fun filterByPokemonType(filter: String) {
        pokemonTypeFilter.value = filter
    }

    fun filterByRange(from: Int, to: Int) {
        rangeFilter.value = arrayListOf<Int>().apply {
            add(from)
            add(to)
        }
    }

    fun noFilter() {
        noFilter.value = true
    }

    fun handleError(exception: Throwable) {
        error.value = exception.toString()
    }
}