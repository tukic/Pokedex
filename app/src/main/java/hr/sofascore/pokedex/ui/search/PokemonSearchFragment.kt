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
import hr.sofascore.pokedex.databinding.FragmentPokemonSearchBinding
import hr.sofascore.pokedex.model.shared.PokemonResponse
import hr.sofascore.pokedex.ui.adapter.FavoritePokemonAdapter
import hr.sofascore.pokedex.ui.adapter.PagedPokemonAdapter
import hr.sofascore.pokedex.ui.adapter.StartPokemonActivityListener
import hr.sofascore.pokedex.ui.pokemon.POKEMON_EXTRA
import hr.sofascore.pokedex.ui.pokemon.PokemonActivity
import hr.sofascore.pokedex.ui.views.FavouritePokemonListener
import hr.sofascore.pokedex.viewmodels.PokemonViewModel


class PokemonSearchFragment : Fragment(), FavouritePokemonListener, StartPokemonActivityListener {

    private val pokemonViewModel: PokemonViewModel by activityViewModels()
    private val adapter = PagedPokemonAdapter(arrayListOf(), this, this)

    private var _binding: FragmentPokemonSearchBinding? = null
    private val binding get() = _binding!!

    var pagingAdapterOn = true

    var filteredPokemons: List<PokemonResponse> = arrayListOf()

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
        pokemonViewModel.pokemonPagedList.observe(
            this as LifecycleOwner,
            {
                adapter.submitList(it)
            }
        )

        pokemonViewModel.favouritePokemon.observe(
            this as LifecycleOwner,
            { favoritePokemons ->
                adapter.updateFavouritePokemon(favoritePokemons as ArrayList<PokemonResponse>)
                if (pagingAdapterOn) {
                    binding.recyclerView.adapter = adapter
                } else {
                    pokemonViewModel.filteredPokemons.value?.let {
                        binding.recyclerView.adapter =
                            FavoritePokemonAdapter(
                                requireContext(),
                                it,
                                this,
                                this,
                                favoritePokemons
                            )
                    }
                }
            }
        )

        binding.searchPokemonTextView.doOnTextChanged { text, start, before, count ->
            binding.searchIcon.visibility = View.GONE
            pagingAdapterOn = false
            when (binding.filterRadioGroup.checkedRadioButtonId) {
                2 -> {
                    if (count > 0 && start + count == 2) {
                        pokemonViewModel.getPokemonsFilteredByType(
                            text.toString().trim()
                        )
                    } else if ((start + count > 2) || (start >= 2 && before > 0)) {
                        binding.recyclerView.adapter = FavoritePokemonAdapter(
                            requireContext(),
                            filteredPokemons.filter { it.types!!.any { it.type.name.contains(text.toString()) } }
                                .sortedBy { it.id },
                            this,
                            this,
                            pokemonViewModel.favouritePokemon.value
                        )
                    }
                }
                else -> {
                    if (count > 0 && start + count == 2) {
                        pokemonViewModel.getPokemonsFilteredByName(
                            text.toString().trim()
                        )
                    } else if ((start + count > 2) || (start >= 2 && before > 0)) {
                        filteredPokemons
                        binding.recyclerView.adapter = FavoritePokemonAdapter(
                            requireContext(),
                            filteredPokemons.filter { it.name.contains(text.toString()) },
                            this,
                            this,
                            pokemonViewModel.favouritePokemon.value
                        )
                    }
                }
            }
            if (text.toString().isNotEmpty()) {
                binding.closeIcon.visibility = View.VISIBLE
            }
        }

        binding.filterByNameRadioButton.setOnClickListener {
            if (binding.searchPokemonTextView.text.length >= 2) {
                pokemonViewModel.getPokemonsFilteredByName(
                    binding.searchPokemonTextView.text.toString().trim()
                )
            }
        }

        binding.filterByTypeRadioButton.setOnClickListener {
            if (binding.searchPokemonTextView.text.length >= 2) {
                pokemonViewModel.getPokemonsFilteredByType(
                    binding.searchPokemonTextView.text.toString().trim()
                )
            }
        }

        binding.closeIcon.setOnClickListener {
            binding.closeIcon.visibility = View.GONE
            binding.filterRadioGroup.visibility = View.GONE
            pagingAdapterOn = true
            binding.searchPokemonTextView.text.clear()
            binding.recyclerView.adapter = adapter
            binding.searchIcon.visibility = View.VISIBLE
        }

        pokemonViewModel.filteredPokemons.observe(
            this as LifecycleOwner,
            {
                binding.recyclerView.adapter = FavoritePokemonAdapter(
                    requireContext(),
                    it,
                    this,
                    this,
                    pokemonViewModel.favouritePokemon.value
                )
                filteredPokemons = it
            }
        )

        binding.filterIcon.setOnClickListener {
            binding.filterRadioGroup.visibility = View.VISIBLE
        }

        return view
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