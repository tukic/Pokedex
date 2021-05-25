package hr.sofascore.pokedex.ui.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import hr.sofascore.pokedex.R
import hr.sofascore.pokedex.databinding.SmallPokemonItemBinding
import hr.sofascore.pokedex.model.shared.PokemonResponse
import hr.sofascore.pokedex.ui.pokemon.POKEMON_EXTRA
import hr.sofascore.pokedex.ui.pokemon.PokemonActivity

class PokemonGridAdapter(
    val context: Context,
    val pokemons: List<PokemonResponse>
) : RecyclerView.Adapter<PokemonGridAdapter.ViewHolder>() {

    class ViewHolder(view: View, viewGroup: ViewGroup) : RecyclerView.ViewHolder(view) {
        val binding = SmallPokemonItemBinding.bind(view)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.small_pokemon_item, viewGroup, false)

        return ViewHolder(view, viewGroup)
    }


    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val pokemon = pokemons[position]

        viewHolder.binding.pokemonImageView.load(pokemon.getImageURL())
        viewHolder.binding.pokemonNameTextView.text = pokemon.name

        viewHolder.binding.root.setOnClickListener {
            val intent = Intent(context, PokemonActivity::class.java).apply {
                putExtra(POKEMON_EXTRA, pokemon)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = pokemons.size

    companion object {
        class MarginItemDecoration(private val spaceSize: Int) : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect, view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                with(outRect) {
                    if (parent.getChildAdapterPosition(view) / 3 == 0) {
                        top = spaceSize
                    }
                    if (parent.getChildAdapterPosition(view) % 3 == 0) {
                        left = spaceSize
                    }
                    right = spaceSize
                    bottom = spaceSize
                }
            }
        }
    }
}