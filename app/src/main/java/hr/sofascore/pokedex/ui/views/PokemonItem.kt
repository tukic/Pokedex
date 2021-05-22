package hr.sofascore.pokedex.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import hr.sofascore.pokedex.R
import hr.sofascore.pokedex.databinding.PokemonItemBinding

class PokemonItem(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    private val binding: PokemonItemBinding

    val pokemonNameTextView: TextView
    val pokemonNumTextView: TextView
    val pokemonImageView: ImageView
    val starIconImageView: ImageView

    init {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.pokemon_item, null, false)

        binding = PokemonItemBinding.bind(view)
        addView(binding.root)

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.PokemonItem,
            0, 0
        ).apply {
            pokemonNameTextView = binding.pokemonNameTextView
            pokemonNumTextView = binding.pokedexNumTextView
            pokemonImageView = binding.pokemonImageView
            starIconImageView = binding.starIconImageView
        }
    }
}