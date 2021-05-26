package hr.sofascore.pokedex.ui.type

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import hr.sofascore.pokedex.R
import hr.sofascore.pokedex.databinding.FragmentDamageOverviewBinding
import hr.sofascore.pokedex.databinding.FragmentPokemonSearchBinding
import hr.sofascore.pokedex.model.shared.PokemonType
import hr.sofascore.pokedex.ui.adapter.PokemonTypeAdapter
import hr.sofascore.pokedex.viewmodels.TypeViewModel
import kotlinx.coroutines.newFixedThreadPoolContext
import java.util.stream.Collectors

class DamageOverview(val pokemonType: PokemonType) : Fragment() {

    val typeViewModel by activityViewModels<TypeViewModel>()

    private var _binding: FragmentDamageOverviewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)

        _binding = FragmentDamageOverviewBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.damageRecycler.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL).apply {
            gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
        }

        typeViewModel.pokemonTypesByUrls.observe(
            this as LifecycleOwner,
            {
                binding.damageRecycler.adapter = PokemonTypeAdapter(requireContext(), it)
            }
        )
        typeViewModel.getTypesByUrls(
            pokemonType.damage_relations.half_damage_to.map { it.url }
        )

        return view
    }
}