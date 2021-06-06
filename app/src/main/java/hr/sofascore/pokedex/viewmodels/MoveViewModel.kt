package hr.sofascore.pokedex.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.sofascore.pokedex.model.networking.Network
import hr.sofascore.pokedex.model.shared.PokemonMove
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class MoveViewModel : ViewModel() {
    val move = MutableLiveData<List<PokemonMove>>()

    val error = MutableLiveData<String>()
    fun getMoves(urls: List<String>) {
        val handler = CoroutineExceptionHandler { context, exception ->
            handleError(exception)
        }
        viewModelScope.launch(handler) {
            val async = urls.map {
                async {
                    Network().getService().getPokemonMove(it).body().apply {
                        this?.damage_class?.url?.let {
                            this.damage_class_detail =
                                Network().getService().getPokemonMoveDamageClass(it).body()
                        }
                    }
                }
            }
            move.value = async.awaitAll().filterNotNull()
        }
    }

    fun handleError(exception: Throwable) {
        error.value = exception.toString()
    }
}
