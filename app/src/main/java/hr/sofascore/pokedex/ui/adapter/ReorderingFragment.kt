package hr.sofascore.pokedex.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import hr.sofascore.pokedex.model.shared.PokemonResponse

interface ReorderingFragment{
    fun startDragging(viewHolder: RecyclerView.ViewHolder)
    fun updateReorderedPokemons(pokemons: List<PokemonResponse>)
}