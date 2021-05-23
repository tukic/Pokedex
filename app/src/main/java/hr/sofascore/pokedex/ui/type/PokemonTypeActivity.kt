package hr.sofascore.pokedex.ui.type

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import hr.sofascore.pokedex.databinding.ActivityPokemonTypeBinding
import hr.sofascore.pokedex.model.shared.PokemonType

const val POKEMON_TYPE_EXTRA = "POKEMON_TYPE_EXTRA"

class PokemonTypeActivity : AppCompatActivity() {

    lateinit var binding: ActivityPokemonTypeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        val pokemonType = intent.getSerializableExtra(POKEMON_TYPE_EXTRA) as PokemonType
        setTheme(pokemonType.type.getTypeTheme())
        super.onCreate(savedInstanceState)

        binding = ActivityPokemonTypeBinding.inflate(layoutInflater)
        binding.title.text = pokemonType.type.name
        setContentView(binding.root)
    }
}