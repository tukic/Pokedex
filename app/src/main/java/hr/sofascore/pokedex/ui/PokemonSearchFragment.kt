package hr.sofascore.pokedex.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import hr.sofascore.pokedex.databinding.FragmentPokemonSearchBinding
import hr.sofascore.pokedex.ui.adapter.PagedPokemonAdapter
import hr.sofascore.pokedex.viewmodels.PokemonViewModel


class PokemonSearchFragment : Fragment() {

    private val pokemonViewModel: PokemonViewModel by activityViewModels()
    private val adapter = PagedPokemonAdapter()

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

        return view
    }
}