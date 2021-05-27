package hr.sofascore.pokedex.ui.pokemon

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import coil.api.load
import hr.sofascore.pokedex.R
import hr.sofascore.pokedex.databinding.ActivityPokemonBinding
import hr.sofascore.pokedex.model.shared.*
import hr.sofascore.pokedex.ui.adapter.EvolutionAdapter
import hr.sofascore.pokedex.ui.adapter.PokemonTypeAdapter
import hr.sofascore.pokedex.viewmodels.EvolutionViewModel
import hr.sofascore.pokedex.viewmodels.PokemonViewModel
import hr.sofascore.pokedex.viewmodels.TypeViewModel
import java.util.*
import java.util.stream.Collectors

const val POKEMON_EXTRA = "POKEMON"

class PokemonActivity : AppCompatActivity() {

    lateinit var binding: ActivityPokemonBinding

    val pokemonViewModel: PokemonViewModel by viewModels<PokemonViewModel>()
    val evolutionViewModel: EvolutionViewModel by viewModels<EvolutionViewModel>()
    val typeViewModel: TypeViewModel by viewModels<TypeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_PokemonActivity)
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

        pokemon.types?.let {
            if (it.isEmpty()) {
                pokemonViewModel.getPokemon(pokemon.id)
            }
        }
        pokemonViewModel.pokemon.observe(
            this as LifecycleOwner,
            {
                typeViewModel.getPokemonTypes(it)
                evolutionViewModel.getEvolutions(it.id)
                setStats(it)
                setAbilites(it)
            }
        )

        binding.pokemonToolbar.title = pokemon.name.capitalize(Locale.getDefault())

        binding.collapsingToolbarLayout.title = pokemon.name.capitalize(Locale.getDefault())
        binding.collapsingToolbarLayout.setExpandedTitleTextColor(getColorStateList(R.color.black))
        binding.collapsingToolbarLayout.setCollapsedTitleTextColor(getColorStateList(R.color.black))

        binding.pokedexNumTextView.text = pokemon.getFormattedId()
        binding.pokemonImageView.load(pokemon.getImageURL())

        binding.weightTextView.text = pokemon.getFormattedWeight()
        binding.heightTextView.text = pokemon.getFormattedHeight()

        setAbilites(pokemon)
        setStats(pokemon)

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

        binding.pokemonTypeRecycler.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.pokemonTypeRecycler.itemAnimator = DefaultItemAnimator()

        typeViewModel.pokemonTypes.observe(
            this as LifecycleOwner,
            {
                binding.pokemonTypeRecycler.adapter = PokemonTypeAdapter(this, it)
            }
        )
        typeViewModel.getPokemonTypes(pokemon)
        /*
        pokemonViewModel.pokemon.observe(
            this as LifecycleOwner,
            {
                pokemon.types?.let {
                    binding.pokemonTypeRecycler.adapter = PokemonTypeAdapter(
                        this, it.stream().map {
                            PokemonTypeDescription(it.type.name, it.type.url)
                        }.collect(Collectors.toList())
                    )
                }
            }
        )
         */

        //pokemonViewModel.getPokemon(pokemon.id)

        binding.evolutionRecyclerView.layoutManager = LinearLayoutManager(this)
        evolutionViewModel.evolutions.observe(
            this as LifecycleOwner,
            {
                binding.evolutionRecyclerView.adapter = EvolutionAdapter(this, pokemon, it)
            }
        )
        evolutionViewModel.getEvolutions(pokemon.id)

    }

    private fun setAbilites(pokemon: PokemonResponse) {
        pokemon.abilities?.filter { !it.is_hidden }?.let {
            if (it.isNotEmpty()) {
                binding.abilityTextView.text = it[0].ability.name.capitalize(Locale.getDefault())
            }
        }

        pokemon.abilities?.filter { it.is_hidden }?.let {
            if (it.isNotEmpty()) {
                binding.hiddenAbilityTextView.text =
                    it[0].ability.name.capitalize(Locale.getDefault())
            }
        }
    }

    fun setStats(pokemon: PokemonResponse) {
        pokemon.getStat(HP_STAT)?.let {
            binding.hpBaseStat.baseStatsValueTextView.text = it.base_stat.toString()
            binding.hpBaseStat.statsProgressBar.progress = it.base_stat
        }
        pokemon.getStat(ATTACK_STAT)?.let {
            binding.attackBaseStat.baseStatsValueTextView.text = it.base_stat.toString()
            binding.attackBaseStat.statsProgressBar.progress = it.base_stat
        }
        pokemon.getStat(DEFENSE_STAT)?.let {
            binding.defenseBaseStat.baseStatsValueTextView.text = it.base_stat.toString()
            binding.defenseBaseStat.statsProgressBar.progress = it.base_stat
        }
        pokemon.getStat(SPECIAL_ATTACK_STAT)?.let {
            binding.spAttackBaseStat.baseStatsValueTextView.text = it.base_stat.toString()
            binding.spAttackBaseStat.statsProgressBar.progress = it.base_stat
        }
        pokemon.getStat(SPECIAL_DEFENSE_STAT)?.let {
            binding.spDefenseBaseStat.baseStatsValueTextView.text = it.base_stat.toString()
            binding.spDefenseBaseStat.statsProgressBar.progress = it.base_stat
        }
        pokemon.getStat(SPEED_STAT)?.let {
            binding.speedBaseStat.baseStatsValueTextView.text = it.base_stat.toString()
            binding.speedBaseStat.statsProgressBar.progress = it.base_stat
        }
        binding.totalStatsValueTextView.text = pokemon.getTotalStats().toString()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}