package hr.sofascore.pokedex.ui.pokemon

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import coil.api.load
import hr.sofascore.pokedex.R
import hr.sofascore.pokedex.databinding.ActivityPokemonBinding
import hr.sofascore.pokedex.model.shared.*
import hr.sofascore.pokedex.ui.adapter.EvolutionAdapter
import hr.sofascore.pokedex.ui.adapter.PokemonTypeAdapter
import hr.sofascore.pokedex.viewmodels.EvolutionViewModel
import hr.sofascore.pokedex.viewmodels.LanguageViewModel
import hr.sofascore.pokedex.viewmodels.PokemonViewModel
import hr.sofascore.pokedex.viewmodels.TypeViewModel
import java.util.*

const val POKEMON_EXTRA = "POKEMON"

class PokemonActivity : AppCompatActivity() {

    lateinit var binding: ActivityPokemonBinding

    val pokemonViewModel: PokemonViewModel by viewModels<PokemonViewModel>()
    val evolutionViewModel: EvolutionViewModel by viewModels<EvolutionViewModel>()
    val typeViewModel: TypeViewModel by viewModels<TypeViewModel>()
    val languageViewModel: LanguageViewModel by viewModels<LanguageViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_PokemonActivity)
        super.onCreate(savedInstanceState)

        binding = ActivityPokemonBinding.inflate(layoutInflater)
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
                it.abilities?.find { !it.is_hidden }?.ability?.url?.let {
                    languageViewModel.translateAbility(this, it)
                }
                it.abilities?.find { it.is_hidden }?.ability?.url?.let {
                    languageViewModel.translateHiddenAbility(this, it)
                }
                setStats(it)
                it.stats?.map { it.stat.url }?.let {
                    languageViewModel.translateStats(it)
                }
                //setAbilites(it)
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

        //setAbilites(pokemon)
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
        pokemonViewModel.getFavouritePokemonSortedByByFavoriteIndex(this)

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
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        //StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.pokemonTypeRecycler.itemAnimator = DefaultItemAnimator()

        typeViewModel.pokemonTypes.observe(
            this as LifecycleOwner,
            {
                binding.pokemonTypeRecycler.adapter = PokemonTypeAdapter(this, it)
            }
        )
        typeViewModel.getPokemonTypes(pokemon)

        val evolutionChains: MutableList<EvolutionHelper> = arrayListOf()
        evolutionViewModel.evolution.observe(
            this as LifecycleOwner,
            {
                findEvolutions(evolutionChains, it.chain.evolves_to)

                /* add unevolved pokemon to chain */
                it.chain.evolves_to.forEach { evolutions ->
                    val next = evolutionChains.find { next ->
                        it.chain.evolves_to!!.any {
                            next.name == it.species.name
                        }
                    }
                    evolutionChains.remove(next)
                    val new = EvolutionHelper(it.chain.species.name, next)
                    evolutionChains.add(new)
                }

                val pokemonNames = evolutionChains.flatMap { chain ->
                    arrayListOf<String>().apply { addAll(chain.getAllPokemonNames()) }
                }
                pokemonViewModel.getEvolutionPokemon(pokemonNames)
            }

        )
        evolutionViewModel.getEvolution(pokemon.id)

        pokemonViewModel.evolutionPokemons.observe(
            this as LifecycleOwner,
            { pokemonResponses ->
                evolutionChains.forEach {
                    var current: EvolutionHelper? = it
                    while (current != null) {
                        current.pokemonResponse =
                            pokemonResponses.find { it.name == current!!.name }
                        current = current.evolves_to
                    }
                }
                binding.evolutionRecyclerView.layoutManager = LinearLayoutManager(this)
                binding.evolutionRecyclerView.adapter =
                    EvolutionAdapter(this, evolutionChains, pokemonViewModel, pokemon.id)
            }
        )
        setPokeathlonStats()

        languageViewModel.abilityTranslation.observe(
            this as LifecycleOwner,
            {
                binding.abilityTextView.text = it
            }
        )
        languageViewModel.hiddenAbilityTranslation.observe(
            this as LifecycleOwner,
            {
                binding.hiddenAbilityTextView.text = it
            }
        )

        languageViewModel.statsTranslations.observe(
            this as LifecycleOwner,
            {
                it.forEach {
                    when (it.name) {
                        "hp" -> binding.hpBaseStat.baseStatsLabelTextView.text =
                            "${it.getName(this)}:"
                        "attack" -> binding.attackBaseStat.baseStatsLabelTextView.text =
                            "${it.getName(this)}:"
                        "defense" -> binding.defenseBaseStat.baseStatsLabelTextView.text =
                            "${it.getName(this)}:"
                        "special-attack" -> binding.spAttackBaseStat.baseStatsLabelTextView.text =
                            "${it.getName(this)}:"
                        "special-defense" -> binding.spDefenseBaseStat.baseStatsLabelTextView.text =
                            "${it.getName(this)}:"
                        "speed" -> binding.speedBaseStat.baseStatsLabelTextView.text =
                            "${it.getName(this)}:"
                    }
                }
            }
        )

        pokemon.stats?.map { it.stat.url }?.let {
            languageViewModel.translateStats(it)
        }

        pokemon.abilities?.find { !it.is_hidden }?.ability?.url?.let {
            languageViewModel.translateAbility(this, it)
        }
        pokemon.abilities?.find { it.is_hidden }?.ability?.url?.let {
            languageViewModel.translateHiddenAbility(this, it)
        }

    }

    private fun findEvolutions(
        evolutionChains: MutableList<EvolutionHelper>,
        evolutions: List<EvolutionDescription>?
    ) {
        evolutions?.let { evolutions ->
            if (evolutions.flatMap { arrayListOf<EvolutionDescription>().apply { addAll(it.evolves_to!!) } }
                    .isEmpty()) {
                evolutions.forEach { evolution ->
                    evolutionChains.add(
                        EvolutionHelper(
                            evolution.species.name,
                            minLevel = if (evolution.evolution_details != null && evolution.evolution_details[0] != null) evolution.evolution_details[0].min_level else null
                        )
                    )
                }
            } else {
                evolutions.forEach { evolution ->
                    findEvolutions(evolutionChains, evolution.evolves_to)
                    val next = evolutionChains.find { next ->
                        evolution.evolves_to!!.any {
                            next.name == it.species.name
                        }
                    }
                    evolutionChains.remove(next)
                    val new = EvolutionHelper(
                        evolution.species.name,
                        next,
                        minLevel = if (evolution.evolution_details != null && evolution.evolution_details[0] != null) evolution.evolution_details[0].min_level else null
                    )
                    evolutionChains.add(new)
                }
            }
        }

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

    fun setPokeathlonStats() {
        //mock data
        val speed = 2
        val maxSpeed = 3
        val power = 2
        val maxPower = 3
        val skill = 3
        val maxSkill = 4
        val stamina = 3
        val maxStamina = 4
        val jump = 3
        val maxJump = 3
        val total = speed + power + skill + stamina + jump
        val maxTotal = maxSpeed + maxPower + maxSkill + maxStamina + maxJump

        binding.speedPokeathlonStatsItem.addStars(
            speed,
            maxSpeed - speed,
            R.color.flat_pokemon_type_poison
        )
        binding.powerPokeathlonStatsItem.addStars(power, maxPower - power, R.color.error)
        binding.skillPokeathlonStatsItem.addStars(skill, maxSkill - skill, R.color.tint_secondary)
        binding.staminaPokeathlonStatsItem.addStars(
            stamina,
            maxStamina - stamina,
            R.color.tint_primary
        )
        binding.jumpPokeathlonStatsItem.addStars(jump, maxJump - jump, R.color.success)
        binding.totalPokeathlonStatsItem.addTotalStars(
            total / 5,
            maxTotal / 5 - total / 5,
            total,
            maxTotal,
            R.color.cold_gray
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}