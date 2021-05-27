package hr.sofascore.pokedex.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import hr.sofascore.pokedex.databinding.FragmentFavoritesBinding
import hr.sofascore.pokedex.ui.adapter.FavoritePokemonAdapter
import hr.sofascore.pokedex.viewmodels.PokemonViewModel

class FavoritesFragment : Fragment() {

    val pokemonViewModel by activityViewModels<PokemonViewModel>()

    private var _binding: FragmentFavoritesBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.favoritesRecyclerView.layoutManager = LinearLayoutManager(context)
        pokemonViewModel.favouritePokemon.observe(
            this as LifecycleOwner,
            {
                binding.favoritesRecyclerView.adapter = FavoritePokemonAdapter(requireContext(), it)
            }
        )
        pokemonViewModel.getFavouritePokemon(requireContext())

        return view
    }
}