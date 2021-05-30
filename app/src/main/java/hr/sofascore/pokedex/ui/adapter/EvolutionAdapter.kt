package hr.sofascore.pokedex.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import hr.sofascore.pokedex.R
import hr.sofascore.pokedex.databinding.EvolutionLayoutBinding
import hr.sofascore.pokedex.model.shared.EvolutionHelper
import hr.sofascore.pokedex.util.OrdinalNumberHelper
import hr.sofascore.pokedex.viewmodels.PokemonViewModel
import java.util.*

class EvolutionAdapter(
    val context: Context,
    //val pokemon: PokemonResponse,
    val evolutions: List<EvolutionHelper>,
    val pokemonViewModel: PokemonViewModel
) : RecyclerView.Adapter<EvolutionAdapter.ViewHolder>() {

    class ViewHolder(view: View, viewGroup: ViewGroup) : RecyclerView.ViewHolder(view) {
        val binding = EvolutionLayoutBinding.bind(view)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.evolution_layout, viewGroup, false)

        return ViewHolder(view, viewGroup)
    }


    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val evolution = evolutions[position]

        var next: EvolutionHelper? = evolution
        var evolutionLevel = 0
        while (next != null) {
            val current = next
            val layout = LayoutInflater.from(context).inflate(
                R.layout.evolution_item,
                viewHolder.binding.linear,
                false
            )
            val evolution_level = layout.findViewById<TextView>(R.id.evolution_label_text_view)
            val image = layout.findViewById<ImageView>(R.id.evolution_pokemon_image_view)
            val name = layout.findViewById<TextView>(R.id.evolution_pokemon_name_text_view)
            val type = layout.findViewById<TextView>(R.id.evolution_pokemon_type_text_view)
            name.text = current.name

            current.pokemonResponse?.let { pokemon ->
                image.load(pokemon.getImageURL())
                pokemon.types?.get(0)?.type?.let {
                    type.text = it.name
                    type.backgroundTintList = context.getColorStateList(it.getTypeColor())
                }
            }

            if (evolutionLevel == 0) {
                evolution_level.text = context.getString(R.string.unevolved).toUpperCase(Locale.getDefault())
            } else {
                val transitionView = LayoutInflater.from(context).inflate(
                    R.layout.evolution_item_transition_layout,
                    viewHolder.binding.linear,
                    false
                )
                val transition = transitionView.findViewById<TextView>(R.id.transition_text_view)
                if (current.minLevel != null && current.minLevel > 0) {
                    transition.text = context.getString(R.string.evolution_level, current.minLevel)
                }
                viewHolder.binding.linear.addView(
                    transitionView
                )
                evolution_level.text = context.getString(
                    R.string.evolution_level_formatted,
                    OrdinalNumberHelper.ordinalOf(evolutionLevel)
                )
            }
            viewHolder.binding.linear.addView(
                layout
            )
            next = next.evolves_to
            evolutionLevel++
        }
    }

    override fun getItemCount() = evolutions.size
}