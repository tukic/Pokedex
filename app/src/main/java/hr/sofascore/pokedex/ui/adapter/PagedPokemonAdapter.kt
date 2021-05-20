package hr.sofascore.pokedex.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import hr.sofascore.pokedex.R
import hr.sofascore.pokedex.databinding.PokemonItemBinding
import hr.sofascore.pokedex.model.shared.PokemonResponse
import hr.sofascore.pokedex.util.PokemonDiff
import java.util.*

class PagedPokemonAdapter:
    PagedListAdapter<PokemonResponse, PagedPokemonAdapter.PokemonViewHolder>(PokemonDiff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.pokemon_item, parent, false)
        return PokemonViewHolder(view)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bindPokemon(it)
        }
    }

    class PokemonViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private val binding = PokemonItemBinding.bind(view)

        fun bindPokemon(pokemon: PokemonResponse) {
            binding.pokemonNameTextView.text = pokemon.name.capitalize(Locale.ROOT)
            binding.pokemonImageView.load(pokemonImageURL(pokemon.id))
            binding.pokedexNumTextView.text = "%03d".format(pokemon.id)
        }

        fun pokemonImageURL(id: Int) = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${id}.png"
    }

}