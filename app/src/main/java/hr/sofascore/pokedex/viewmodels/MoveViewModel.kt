package hr.sofascore.pokedex.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.sofascore.pokedex.model.networking.Network
import hr.sofascore.pokedex.model.shared.PokemonMove
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class MoveViewModel : ViewModel() {
    val move = MutableLiveData<List<PokemonMove>>()

    fun getMoves(urls: List<String>) {
        viewModelScope.launch {
            val async = urls.map {
                async {
                    Network().getService().getPokemonMove(it).body().apply {
                        this?.damage_class?.url?.let {
                            this.damage_class_detail = Network().getService().getPokemonMoveDamageClass(it).body()
                        }
                    }
                }
            }
            move.value = async.awaitAll().filterNotNull()
        }
    }
}
