package hr.sofascore.pokedex.ui.search

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.slider.RangeSlider
import com.google.android.material.snackbar.Snackbar
import hr.sofascore.pokedex.R
import hr.sofascore.pokedex.databinding.FragmentPokemonSearchBinding
import hr.sofascore.pokedex.model.shared.PokemonResponse
import hr.sofascore.pokedex.ui.adapter.FavoritePokemonAdapter
import hr.sofascore.pokedex.ui.adapter.PagedPokemonAdapter
import hr.sofascore.pokedex.ui.adapter.StartPokemonActivityListener
import hr.sofascore.pokedex.ui.pokemon.POKEMON_EXTRA
import hr.sofascore.pokedex.ui.pokemon.PokemonActivity
import hr.sofascore.pokedex.ui.views.FavouritePokemonListener
import hr.sofascore.pokedex.ui.views.Snackbars.Companion.configError
import hr.sofascore.pokedex.viewmodels.PokemonViewModel


class PokemonSearchFragment : Fragment(), FavouritePokemonListener, StartPokemonActivityListener {

    private val pokemonViewModel: PokemonViewModel by activityViewModels()
    private val adapter = PagedPokemonAdapter(arrayListOf(), this, this)

    private var _binding: FragmentPokemonSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)

        _binding = FragmentPokemonSearchBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        pokemonViewModel.error.observe(
            this as LifecycleOwner,
            { errorDescription ->
                val snackbar = Snackbar.make(
                    binding.snackbarContainer,
                    errorDescription,
                    Snackbar.LENGTH_LONG
                ).setAction(" ") { actionText ->
                    actionText.visibility = View.GONE
                }
                snackbar.configError(requireContext())
                snackbar.show()
                binding.progressBar.visibility = View.GONE
            }
        )

        adapter.addLoadStateListener { loadType, loadState ->
            if(adapter.itemCount > 1) {
                binding.progressBar.visibility = View.GONE
            }
        }
        pokemonViewModel.pokemonPagedList.observe(
            this as LifecycleOwner,
            {
                adapter.submitList(it)
            }
        )
        pokemonViewModel.noFilter()

        pokemonViewModel.pokemonRangeFilteredPagedList.observe(
            this as LifecycleOwner,
            {
                adapter.submitList(it)
            }
        )

        pokemonViewModel.pokemonNameFilteredPagedList.observe(
            this as LifecycleOwner,
            {
                adapter.submitList(it)
            }
        )

        pokemonViewModel.pokemonTypeFilteredPagedList.observe(
            this as LifecycleOwner,
            {
                adapter.submitList(it)
            }
        )

        pokemonViewModel.favouritePokemon.observe(
            this as LifecycleOwner,
            { favoritePokemons ->
                adapter.updateFavouritePokemon(favoritePokemons as ArrayList<PokemonResponse>)
                binding.recyclerView.adapter = adapter
            }
        )

        binding.searchPokemonTextView.doOnTextChanged { text, start, before, count ->
            binding.searchIcon.visibility = View.GONE
            when (binding.filterRadioGroup.checkedRadioButtonId) {
                R.id.filter_by_type_radio_button -> {
                    if (binding.searchPokemonTextView.text.length > 1) {
                        pokemonViewModel.filterByPokemonType(
                            text.toString().trim().toLowerCase()
                        )
                    }
                }
                R.id.filter_by_range -> {
                    // do nothing
                }
                else -> {
                    if (binding.searchPokemonTextView.text.length > 1) {
                        pokemonViewModel.filterByPokemonName(
                            text.toString().trim().toLowerCase()
                        )
                    }
                }
            }
            if (text.toString().isNotEmpty()) {
                binding.closeIcon.visibility = View.VISIBLE
            }
        }

        binding.filterByNameRadioButton.setOnClickListener {
            filterByTextRadioSetup()
            if (binding.searchPokemonTextView.text.length > 1) {
                pokemonViewModel.filterByPokemonName(
                    binding.searchPokemonTextView.text.toString().trim().toLowerCase()
                )
            }
        }

        binding.filterByTypeRadioButton.setOnClickListener {
            filterByTextRadioSetup()
            if (binding.searchPokemonTextView.text.length > 1) {
                pokemonViewModel.filterByPokemonType(
                    binding.searchPokemonTextView.text.toString().trim().toLowerCase()
                )
            }
        }

        binding.selectRangeBar.addOnSliderTouchListener(
            object : RangeSlider.OnSliderTouchListener {
                override fun onStartTrackingTouch(slider: RangeSlider) = Unit
                override fun onStopTrackingTouch(slider: RangeSlider) {
                    pokemonViewModel.filterByRange(
                        slider.values[0].toInt(),
                        slider.values[1].toInt()
                    )
                }
            }
        )

        binding.closeIcon.setOnClickListener {
            binding.closeIcon.visibility = View.GONE
            binding.filterRadioGroup.visibility = View.GONE
            binding.searchPokemonTextView.isEnabled = true
            removeRangeSlider()
            pokemonViewModel.noFilter()
            binding.searchPokemonTextView.text.clear()
            binding.recyclerView.adapter = adapter
            binding.searchIcon.visibility = View.VISIBLE
        }

        pokemonViewModel.pokemonCount.observe(
            this as LifecycleOwner,
            {
                binding.selectRangeBar.valueTo = it.toFloat()
                binding.selectRangeBar.setValues(0F, it.toFloat())
            }
        )
        pokemonViewModel.getPokemonCount()

        binding.filterIcon.setOnClickListener {
            binding.filterByNameRadioButton.isChecked = true
            setRecyclerPaddingToFilterRadioButtons()
            binding.filterRadioGroup.visibility = View.VISIBLE
        }

        binding.filterByRange.setOnClickListener {
            binding.selectRangeBar.visibility = View.VISIBLE
            binding.searchPokemonTextView.isEnabled = false
            binding.searchPokemonTextView.text.clear()
            binding.recyclerView.setPadding(
                0,
                resources.getDimensionPixelSize(R.dimen.pokemon_search_top_margin),
                0,
                resources.getDimensionPixelSize(R.dimen.pokemon_search_bottom_margin_range_filter)
            )
            binding.searchIcon.visibility = View.GONE
            binding.closeIcon.visibility = View.VISIBLE
            binding.searchPokemonTextView.hint = ""
        }

        return view
    }

    private fun filterByTextRadioSetup() {
        binding.searchPokemonTextView.isEnabled = true
        binding.searchIcon.visibility = View.GONE
        removeRangeSlider()
        setRecyclerPaddingToFilterRadioButtons()
        binding.closeIcon.visibility = View.VISIBLE
    }

    private fun setRecyclerPaddingToFilterRadioButtons() {
        binding.recyclerView.setPadding(
            0,
            resources.getDimensionPixelSize(R.dimen.pokemon_search_top_margin),
            0,
            resources.getDimensionPixelSize(R.dimen.pokemon_search_bottom_margin_filter)
        )
    }

    private fun removeRangeSlider() {
        binding.selectRangeBar.visibility = View.GONE
        binding.recyclerView.setPadding(
            0,
            resources.getDimensionPixelSize(R.dimen.pokemon_search_top_margin),
            0,
            resources.getDimensionPixelSize(R.dimen.pokemon_search_bottom_margin)
        )
        binding.searchPokemonTextView.hint = getString(R.string.search_pokemons)
    }

    override fun favouritePokemonAdded(pokemon: PokemonResponse) {
        pokemonViewModel.insertFavouritePokemon(requireContext(), pokemon)
    }

    override fun favouritePokemonRemoved(pokemon: PokemonResponse) {
        pokemonViewModel.deleteFavouritePokemon(requireContext(), pokemon)
    }

    override fun updateFavouritePokemon(): List<PokemonResponse> {
        return pokemonViewModel.favouritePokemon.value as List<PokemonResponse>
    }

    override fun startActivity(pokemon: PokemonResponse) {
        val intent = Intent(context, PokemonActivity::class.java).apply {
            putExtra(POKEMON_EXTRA, pokemon)
        }
        context?.startActivity(intent)
    }

    override fun onStart() {
        pokemonViewModel.getFavouritePokemonSortedByByFavoriteIndex(requireContext())
        super.onStart()
    }
}