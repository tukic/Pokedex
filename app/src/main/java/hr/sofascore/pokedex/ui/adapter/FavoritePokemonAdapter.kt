package hr.sofascore.pokedex.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import hr.sofascore.pokedex.R
import hr.sofascore.pokedex.databinding.PokemonItemBinding
import hr.sofascore.pokedex.model.shared.PokemonResponse
import hr.sofascore.pokedex.ui.views.FavouritePokemonListener

class FavoritePokemonAdapter(
    val context: Context,
    val pokemons: List<PokemonResponse>,
    private val favouritePokemonListener: FavouritePokemonListener,
    private val pokemonActivityListener: StartPokemonActivityListener,
    private val favoritePokemons: List<PokemonResponse>? = null
) : RecyclerView.Adapter<FavoritePokemonAdapter.ViewHolder>() {

    class ViewHolder(view: View, viewGroup: ViewGroup) : RecyclerView.ViewHolder(view) {
        val binding = PokemonItemBinding.bind(view)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.pokemon_item, viewGroup, false)

        return ViewHolder(view, viewGroup)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val pokemon = pokemons[position]

        viewHolder.binding.pokemonImageView.load(pokemon.getImageURL())
        viewHolder.binding.pokemonNameTextView.text = pokemon.name
        viewHolder.binding.pokedexNumTextView.text = pokemon.getFormattedId()
        viewHolder.binding.starIconImageView.load(R.drawable.ic_star_1)

        viewHolder.binding.root.setOnClickListener {
            pokemonActivityListener.startActivity(pokemon)
        }

        viewHolder.binding.starIconImageView.setOnClickListener {
            favouritePokemonListener.favouritePokemonRemoved(pokemon)
        }

        favoritePokemons?.let {
            if (it.any { it.id == pokemon.id }) {
                viewHolder.binding.starIconImageView.load(R.drawable.ic_star_1)
            } else {
                viewHolder.binding.starIconImageView.load(R.drawable.ic_star_0)
            }
        }

        viewHolder.binding.starIconImageView.setOnClickListener {
            favoritePokemons?.let {
                if (it.any { it.id == pokemon.id }) {
                    favouritePokemonListener.favouritePokemonRemoved(pokemon)
                } else {
                    favouritePokemonListener.favouritePokemonAdded(pokemon)
                }
            }
        }
    }

    override fun getItemCount() = pokemons.size
}