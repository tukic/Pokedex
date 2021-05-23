package hr.sofascore.pokedex.ui.search

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import hr.sofascore.pokedex.databinding.FragmentPokemonSearchBinding
import hr.sofascore.pokedex.model.shared.PokemonResponse
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)

        _binding = FragmentPokemonSearchBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.searchIcon.setOnClickListener {
            pokemonViewModel.getPokemon(
                binding.searchPokemonTextView.text.toString()
            )
        }

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
            {
                adapter.updateFavouritePokemon(it as ArrayList<PokemonResponse>)
            }
        )

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
        pokemonViewModel.getFavouritePokemon(requireContext())
        super.onStart()
    }
}