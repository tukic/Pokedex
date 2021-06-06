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
import hr.sofascore.pokedex.viewmodels.datasource.NameFilteringPokemonDataSource
import hr.sofascore.pokedex.viewmodels.datasource.PokemonDataSource
import hr.sofascore.pokedex.viewmodels.datasource.RangeFilteringPokemonDataSource
import hr.sofascore.pokedex.viewmodels.datasource.TypeFilteringPokemonDataSource
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

const val pageSize = 20
class PokemonViewModel : ViewModel() {

    val pokemon = MutableLiveData<PokemonResponse>()
    val pokemonPagedList: LiveData<PagedList<PokemonResponse>>
    val pokemonNameFilteredPagedList: LiveData<PagedList<PokemonResponse>>
    val pokemonTypeFilteredPagedList: LiveData<PagedList<PokemonResponse>>
    val pokemonRangeFilteredPagedList: LiveData<PagedList<PokemonResponse>>

    val favouritePokemon = MutableLiveData<List<PokemonResponse>>()

    val pokemonCount = MutableLiveData<Int>()

    val evolutionPokemons = MutableLiveData<List<PokemonResponse>>()

    private val noFilter = MutableLiveData<Boolean>()
    private val pokemonNameFilter = MutableLiveData<String>()
    private val pokemonTypeFilter = MutableLiveData<String>()
    private val rangeFilter = MutableLiveData<List<Int>>()

    val error = MutableLiveData<String>()

    init {
        evolutionPokemons.value = arrayListOf<PokemonResponse>()
        val config = PagedList.Config.Builder().setPageSize(pageSize).setEnablePlaceholders(false).build()

        pokemonPagedList =
            noFilter.switchMap {
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
            pokemon.value = Network().getService().getPokemonByName(name).body()
        }
    }

    fun getPokemon(id: Int) {
        val handler = CoroutineExceptionHandler { context, exception ->
            handleError(exception)
        }
        viewModelScope.launch(handler) {
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

        }
    }

    fun insertFavouritePokemon(context: Context, pokemon: PokemonResponse) {
        val handler = CoroutineExceptionHandler { context, exception ->
            handleError(exception)
        }
        viewModelScope.launch(handler) {
            val index =
                PokemonDatabase.getDatabase(context)?.pokemonDao()?.getMaxFavoriteIndex()
            if (index != null) {
                pokemon.favoriteIndex = index + 1
            }
            PokemonDatabase.getDatabase(context)?.pokemonDao()?.insertPokemon(pokemon)
            getFavouritePokemonSortedByByFavoriteIndex(context)
        }
    }

    fun getFavouritePokemonSortedByByFavoriteIndex(context: Context) {
        val handler = CoroutineExceptionHandler { context, exception ->
            handleError(exception)
        }
        viewModelScope.launch(handler) {
            favouritePokemon.value =
                PokemonDatabase.getDatabase(context)?.pokemonDao()
                    ?.getAllPokemonSortedByFavoriteIndex()

        }
    }

    fun deleteFavouritePokemon(context: Context, pokemon: PokemonResponse) {
        val handler = CoroutineExceptionHandler { context, exception ->
            handleError(exception)
        }
        viewModelScope.launch(handler) {
            PokemonDatabase.getDatabase(context)?.pokemonDao()?.deletePokemon(pokemon)
            getFavouritePokemonSortedByByFavoriteIndex(context)
        }
    }

    fun updatePokemon(context: Context, pokemons: List<PokemonResponse>) {
        val handler = CoroutineExceptionHandler { context, exception ->
            handleError(exception)
        }
        viewModelScope.launch(handler) {
            pokemons.forEach {
                async {
                    PokemonDatabase.getDatabase(context)?.pokemonDao()?.updatePokemon(it)
                }
            }
        }
    }

    fun deleteAllPokemons(context: Context) {
        val handler = CoroutineExceptionHandler { context, exception ->
            handleError(exception)
        }
        viewModelScope.launch(handler) {
            PokemonDatabase.getDatabase(context)?.pokemonDao()?.deleteAllPokemons()
            getFavouritePokemonSortedByByFavoriteIndex(context)
        }
    }

    fun getPokemonCount() {
        val handler = CoroutineExceptionHandler { context, exception ->
            handleError(exception)
        }
        viewModelScope.launch(handler) {
            Network().getService().getPagedPokemons(Int.MAX_VALUE).body()?.let {
                pokemonCount.value = it.count
            }
        }
    }

    fun getEvolutionPokemon(names: List<String>) {
        val handler = CoroutineExceptionHandler { context, exception ->
            handleError(exception)
        }
        viewModelScope.launch(handler) {
            val tmp = ArrayList<PokemonResponse>()
            evolutionPokemons.value?.let {
                tmp.addAll(it)
            }
            val async = names.map {
                async {
                    Network().getService().getPokemonByName(it).body().apply {
                        val tmp = arrayListOf<PokemonType>()
                        this?.types?.forEach { type ->
                            type.type.url?.let {
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

    private fun handleError(exception: Throwable) {
        error.value = exception.toString()
    }
}