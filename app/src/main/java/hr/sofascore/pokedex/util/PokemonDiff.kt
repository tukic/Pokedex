package hr.sofascore.pokedex.util

import androidx.recyclerview.widget.DiffUtil
import hr.sofascore.pokedex.model.shared.PokemonResponse

class PokemonDiff: DiffUtil.ItemCallback<PokemonResponse>() {
    override fun areItemsTheSame(oldItem: PokemonResponse, newItem: PokemonResponse) =
        oldItem.id == oldItem.id

    override fun areContentsTheSame(oldItem: PokemonResponse, newItem: PokemonResponse) =
        oldItem.name == newItem.name
}