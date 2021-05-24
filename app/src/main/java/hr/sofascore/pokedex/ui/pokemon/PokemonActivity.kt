package hr.sofascore.pokedex.ui.pokemon

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.GridLayoutManager
import coil.api.load
import hr.sofascore.pokedex.R
import hr.sofascore.pokedex.databinding.ActivityPokemonBinding
import hr.sofascore.pokedex.model.shared.*
import hr.sofascore.pokedex.ui.adapter.PokemonTypeAdapter
import hr.sofascore.pokedex.viewmodels.PokemonViewModel
import java.util.*

const val POKEMON_EXTRA = "POKEMON"

class PokemonActivity : AppCompatActivity() {

    lateinit var binding: ActivityPokemonBinding

    val pokemonViewModel: PokemonViewModel by viewModels<PokemonViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPokemonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setContentView(binding.root)
        setSupportActionBar(binding.pokemonToolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        binding.collapsingToolbarLayout.setExpandedTitleTextColor(getColorStateList(R.color.white))
        binding.collapsingToolbarLayout.setCollapsedTitleTextColor(getColorStateList(R.color.white))

        val pokemon = intent.extras?.getSerializable(POKEMON_EXTRA) as PokemonResponse

        binding.pokemonToolbar.title = pokemon.name.capitalize(Locale.getDefault())

        binding.collapsingToolbarLayout.title = pokemon.name.capitalize(Locale.getDefault())
        binding.collapsingToolbarLayout.setExpandedTitleTextColor(getColorStateList(R.color.black))
        binding.collapsingToolbarLayout.setCollapsedTitleTextColor(getColorStateList(R.color.black))

        binding.pokedexNumTextView.text = pokemon.getFormattedId()
        binding.pokemonImageView.load(pokemon.getImageURL())

        binding.weightTextView.text = pokemon.getFormattedWeight()
        binding.heightTextView.text = pokemon.getFormattedHeight()



        pokemonViewModel.favouritePokemon.observe(
            this as LifecycleOwner,
            {
                if (it.any { it.id == pokemon.id }) {
                    binding.favouritePokemonStarIcon.load(R.drawable.ic_star_1)
                } else {
                    binding.favouritePokemonStarIcon.load(R.drawable.ic_star_0)
                }
            }
        )
        pokemonViewModel.getFavouritePokemon(this)

        binding.favouritePokemonStarIcon.setOnClickListener {
            pokemonViewModel.favouritePokemon.value?.let {
                if (it.any { it.id == pokemon.id }) {
                    pokemonViewModel.deleteFavouritePokemon(baseContext, pokemon)
                } else {
                    pokemonViewModel.insertFavouritePokemon(baseContext, pokemon)
                }
            }
        }

        binding.pokemonTypeAdapter.layoutManager = GridLayoutManager(this, 2)
        pokemonViewModel.pokemon.observe(
            this as LifecycleOwner,
            {
                binding.pokemonTypeAdapter.adapter =
                    pokemon.types?.let { PokemonTypeAdapter(this, it) }
            }
        )
        pokemonViewModel.getPokemon(pokemon.id)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}