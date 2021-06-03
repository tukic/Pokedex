package hr.sofascore.pokedex.ui.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import hr.sofascore.pokedex.R
import hr.sofascore.pokedex.databinding.PokemonItemBinding
import hr.sofascore.pokedex.databinding.SmallPokemonItemBinding
import hr.sofascore.pokedex.model.shared.PokemonResponse
import hr.sofascore.pokedex.ui.pokemon.POKEMON_EXTRA
import hr.sofascore.pokedex.ui.pokemon.PokemonActivity
import hr.sofascore.pokedex.ui.views.FavouritePokemonListener
import hr.sofascore.pokedex.util.PokemonDiff
import hr.sofascore.pokedex.viewmodels.PokemonViewModel
import java.util.*

class PokemonGridAdapter() :
    PagedListAdapter<PokemonResponse, PokemonGridAdapter.GridPokemonViewHolder>(PokemonDiff()) {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): GridPokemonViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.small_pokemon_item, viewGroup, false)

        return GridPokemonViewHolder(view)
    }

    override fun onBindViewHolder(holder: GridPokemonViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bindPokemon(it)
        }
    }

    class GridPokemonViewHolder(
        view: View
    ) : RecyclerView.ViewHolder(view) {
        private val binding = SmallPokemonItemBinding.bind(view)
        fun bindPokemon(pokemon: PokemonResponse) {

            binding.pokemonImageView.load(pokemon.getImageURL())
            binding.pokemonNameTextView.text =
                pokemon.name.capitalize(Locale.getDefault())

        }
    }
}