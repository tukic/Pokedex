package hr.sofascore.pokedex.ui.type.pokemons

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import hr.sofascore.pokedex.R
import hr.sofascore.pokedex.databinding.FragmentMovesBinding
import hr.sofascore.pokedex.databinding.FragmentPokemonsBinding
import hr.sofascore.pokedex.model.shared.PokemonType
import hr.sofascore.pokedex.ui.adapter.PagedPokemonAdapter
import hr.sofascore.pokedex.ui.adapter.PokemonGridAdapter
import hr.sofascore.pokedex.ui.views.Snackbars.Companion.configError
import hr.sofascore.pokedex.viewmodels.EvolutionViewModel
import hr.sofascore.pokedex.viewmodels.PokemonViewModel
import hr.sofascore.pokedex.viewmodels.TypeViewModel

class Pokemons(val type: PokemonType) : Fragment() {

    val pokemonViewModel by activityViewModels<PokemonViewModel>()

    private val adapter = PokemonGridAdapter()

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
        binding.pokemonRecycler.adapter = adapter
        pokemonViewModel.pokemonTypeFilteredPagedList.observe(
            this as LifecycleOwner,
            {
                adapter.submitList(it)
            }
        )
        pokemonViewModel.error.observe(
            this as LifecycleOwner,
            {
                val snackbar = Snackbar.make(
                    binding.snackbarContainer,
                    it,
                    Snackbar.LENGTH_LONG
                ).setAction(" ") {
                    it.visibility = View.GONE
                }
                snackbar.configError(requireContext())
                snackbar.show()
            }
        )

        adapter.addLoadStateListener { loadType, loadState ->
            if(adapter.itemCount > 1) {
                binding.progressBar.visibility = View.GONE
            }
        }

        pokemonViewModel.filterByPokemonType(type.name)
        return view
    }
}