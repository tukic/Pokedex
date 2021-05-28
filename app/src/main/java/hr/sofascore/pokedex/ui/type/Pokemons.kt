package hr.sofascore.pokedex.ui.type

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hr.sofascore.pokedex.R
import hr.sofascore.pokedex.databinding.FragmentMovesBinding
import hr.sofascore.pokedex.databinding.FragmentPokemonsBinding
import hr.sofascore.pokedex.model.shared.PokemonType
import hr.sofascore.pokedex.ui.adapter.PokemonGridAdapter
import hr.sofascore.pokedex.viewmodels.EvolutionViewModel
import hr.sofascore.pokedex.viewmodels.TypeViewModel

class Pokemons(val type: PokemonType) : Fragment() {

    val typeViewModel by activityViewModels<TypeViewModel>()

    private var _binding: FragmentPokemonsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)

        _binding = FragmentPokemonsBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.pokemonRecycler.layoutManager = GridLayoutManager(context, 3)
        typeViewModel.pokemons.observe(
            this as LifecycleOwner,
            {
                binding.pokemonRecycler.adapter = PokemonGridAdapter(requireContext(), it)
            }
        )
        typeViewModel.getPokemons(type.pokemon)
        return view
    }
}