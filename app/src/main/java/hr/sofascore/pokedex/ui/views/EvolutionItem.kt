package hr.sofascore.pokedex.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import hr.sofascore.pokedex.R
import hr.sofascore.pokedex.databinding.EvolutionItemBinding

class EvolutionItem(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    private val binding: EvolutionItemBinding

    val pokemonImageView: ImageView
    val evolutionPokemonTextView: TextView
    val pokemonTypeTextView: TextView

    init {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.evolution_item, null, false)

        binding = EvolutionItemBinding.bind(view)
        addView(binding.root)

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.EvolutionItem,
            0, 0
        ).apply {
            pokemonImageView = binding.evolutionPokemonImageView
            evolutionPokemonTextView = binding.evolutionPokemonNameTextView
            pokemonTypeTextView = binding.evolutionPokemonTypeTextView

            binding.evolutionLabelTextView.text = getString(R.styleable.EvolutionItem_evolution_phase)
        }
    }
}