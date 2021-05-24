package hr.sofascore.pokedex.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import hr.sofascore.pokedex.R
import hr.sofascore.pokedex.databinding.EvolutionLayoutBinding
import hr.sofascore.pokedex.model.shared.EvolutionDescription
import hr.sofascore.pokedex.model.shared.PokemonResponse

class EvolutionAdapter(
    val context: Context,
    val pokemon: PokemonResponse,
    val evolutions: List<EvolutionDescription>
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

        viewHolder.binding.currentEvolutionItem.pokemonImageView.load(pokemon.getImageURL())
        viewHolder.binding.currentEvolutionItem.evolutionPokemonTextView.text = pokemon.name
        pokemon.types?.get(0)?.let {
            viewHolder.binding.currentEvolutionItem.pokemonTypeTextView.text = it.type.name
            viewHolder.binding.currentEvolutionItem.pokemonTypeTextView.backgroundTintList =
                context.getColorStateList(it.type.getTypeColor())
        }

        viewHolder.binding.nextEvolutionItem.evolutionPokemonTextView.text = evolution.species.name
        //evolves_to?.get(0)?.species?.name
        //evolution.evolves_to?.get(0)?.species?.name
        //evolution.evolves_to?.get(0)?.evolves_to?.get(0)?.species?.name

    }

    override fun getItemCount() = evolutions.size
}