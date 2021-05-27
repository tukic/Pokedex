package hr.sofascore.pokedex.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import hr.sofascore.pokedex.R
import hr.sofascore.pokedex.databinding.ReorderPokemonItemBinding
import hr.sofascore.pokedex.model.shared.PokemonResponse
import hr.sofascore.pokedex.ui.views.FavouritePokemonListener

class ReorderFavoritesAdapter(
    val context: Context,
    private val pokemons: MutableList<PokemonResponse>,
    private val pokemonActivityListener: StartPokemonActivityListener,
    private val favoritePokemonListener: FavouritePokemonListener,
    val reorderingFragment: ReorderingFragment
) : RecyclerView.Adapter<ReorderFavoritesAdapter.ViewHolder>() {


    class ViewHolder(view: View, viewGroup: ViewGroup) : RecyclerView.ViewHolder(view) {
        val binding = ReorderPokemonItemBinding.bind(view)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.reorder_pokemon_item, viewGroup, false)

        return ViewHolder(view, viewGroup)
    }


    @SuppressLint("ClickableViewAccessibility")
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
            favoritePokemonListener.favouritePokemonRemoved(pokemon)
        }

        viewHolder.binding.reorderView.setOnTouchListener { view, event ->
            if (event.actionMasked == MotionEvent.ACTION_DOWN) {
                reorderingFragment.startDragging(viewHolder)
            }
            return@setOnTouchListener true
        }
    }

    override fun getItemCount() = pokemons.size

    fun moveItem(from: Int, to: Int) {
        val pokemon = pokemons[from]
        pokemons.removeAt(from)
        pokemons.add(to, pokemon)
        pokemons.forEachIndexed { i, pokemon ->
            pokemon.favoriteIndex = i
        }
        reorderingFragment.updateReorderedPokemons(pokemons)
    }
}