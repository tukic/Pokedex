package hr.sofascore.pokedex.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import hr.sofascore.pokedex.model.db.PokemonDatabase
import hr.sofascore.pokedex.model.networking.Network
import hr.sofascore.pokedex.model.networking.initialPokemonURL
import hr.sofascore.pokedex.model.shared.PokemonResponse
import hr.sofascore.pokedex.ui.PokemonDataSource
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class PokemonViewModel : ViewModel() {

    val pokemon = MutableLiveData<PokemonResponse>()
    val pokemonPagedList: LiveData<PagedList<PokemonResponse>>

    val favouritePokemon = MutableLiveData<List<PokemonResponse>>()

    init {
        val config = PagedList.Config.Builder().setPageSize(20).setEnablePlaceholders(false).build()
        pokemonPagedList = initializePagedList(config).build()
    }

    private fun initializePagedList(config: PagedList.Config): LivePagedListBuilder<String, PokemonResponse> {
        val dataSource = object : DataSource.Factory<String, PokemonResponse>() {
            override fun create(): DataSource<String, PokemonResponse> {
                return PokemonDataSource(initialPokemonURL, viewModelScope)
            }
        }
        return LivePagedListBuilder(dataSource, config)
    }

    fun getPokemon(name: String) {
        viewModelScope.launch {
            pokemon.value = Network().getService().getPokemonByName(name).body()
        }
    }

    fun getPokemon(id: Int) {
        viewModelScope.launch {
            pokemon.value = Network().getService().getPokemonById(id).body()
        }
    }

    fun insertFavouritePokemon(context: Context, pokemon: PokemonResponse) {
        viewModelScope.launch {
            val index = PokemonDatabase.getDatabase(context)?.pokemonDao()?.getMaxFavoriteIndex()
            if(index != null) {
                pokemon.favoriteIndex = index+1
            }
            PokemonDatabase.getDatabase(context)?.pokemonDao()?.insertPokemon(pokemon)
            getFavouritePokemonSortedByByFavoriteIndex(context)
        }
    }

    fun getFavouritePokemonSortedByByFavoriteIndex(context: Context) {
        viewModelScope.launch {
            favouritePokemon.value =
                PokemonDatabase.getDatabase(context)?.pokemonDao()?.getAllPokemonSortedByFavoriteIndex()
        }
    }

    fun deleteFavouritePokemon(context: Context, pokemon: PokemonResponse) {
        viewModelScope.launch {
            PokemonDatabase.getDatabase(context)?.pokemonDao()?.deletePokemon(pokemon)
            getFavouritePokemonSortedByByFavoriteIndex(context)
        }
    }

    fun updatePokemon(context: Context, pokemons: List<PokemonResponse>) {
        viewModelScope.launch {
            pokemons.forEach {
                async {
                    PokemonDatabase.getDatabase(context)?.pokemonDao()?.updatePokemon(it)
                }
            }
        }
    }
}