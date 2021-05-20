package hr.sofascore.pokedex.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import hr.sofascore.pokedex.model.networking.Network
import hr.sofascore.pokedex.model.networking.initialPokemonURL
import hr.sofascore.pokedex.model.shared.PokemonResponse
import hr.sofascore.pokedex.ui.PokemonDataSource
import kotlinx.coroutines.launch

class PokemonViewModel : ViewModel() {

    val pokemon = MutableLiveData<PokemonResponse>()
    val pokemonPagedList: LiveData<PagedList<PokemonResponse>>

    init {
        val config = PagedList.Config.Builder().setPageSize(20).setEnablePlaceholders(false).build()
        pokemonPagedList = initializePagedList(config).build()
    }

    private fun initializePagedList(config: PagedList.Config): LivePagedListBuilder<String, PokemonResponse> {
        val dataSource =  object: DataSource.Factory<String, PokemonResponse>() {
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
}