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

        /*
        binding.damageRecycler.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL).apply {
            gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
        }
        binding.offenseDoubleDamageOverview.damageRecyclerView.addItemDecoration(
            PokemonTypeAdapter.Companion.SpacesItemDecoration(
                resources.getDimensionPixelSize(R.dimen.empty_margin)
            )
        )
         */
        binding.offenseDoubleDamageOverview.damageRecyclerView.layoutManager =
            GridLayoutManager(context, 3)
        binding.offenseHalfDamageOverview.damageRecyclerView.layoutManager =
            GridLayoutManager(context, 3)
        binding.offenseNoDamageOverview.damageRecyclerView.layoutManager =
            GridLayoutManager(context, 3)
        binding.defensiveHalfDamageOverview.damageRecyclerView.layoutManager =
            GridLayoutManager(context, 3)
        binding.defensiveDoubleDamageOverview.damageRecyclerView.layoutManager =
            GridLayoutManager(context, 3)
        binding.defensiveNoDamageOverview.damageRecyclerView.layoutManager =
            GridLayoutManager(context, 3)

        typeViewModel.offensiveDoubleDamageTypes.observe(
            this as LifecycleOwner,
            {
                if(it.isNotEmpty()) {
                    binding.offenseDoubleDamageOverview.damageRecyclerView.adapter =
                        PokemonTypeAdapter(requireContext(), it)
                } else {
                    binding.offenseDoubleDamageOverview.noTypesTextView.visibility = View.VISIBLE
                }
            }
        )
        typeViewModel.offensiveHalfDamageTypes.observe(
            this as LifecycleOwner,
            {
                if(it.isNotEmpty()) {
                    binding.offenseHalfDamageOverview.damageRecyclerView.adapter =
                        PokemonTypeAdapter(requireContext(), it)
                } else {
                    binding.offenseHalfDamageOverview.noTypesTextView.visibility = View.VISIBLE
                }
            }
        )
        typeViewModel.offensiveNoDamageTypes.observe(
            this as LifecycleOwner,
            {
                if(it.isNotEmpty()) {
                    binding.offenseNoDamageOverview.damageRecyclerView.adapter =
                        PokemonTypeAdapter(requireContext(), it)
                } else {
                    binding.offenseNoDamageOverview.noTypesTextView.visibility = View.VISIBLE
                }
            }
        )
        typeViewModel.defensiveHalfDamageTypes.observe(
            this as LifecycleOwner,
            {
                if(it.isNotEmpty()) {
                    binding.defensiveHalfDamageOverview.damageRecyclerView.adapter =
                        PokemonTypeAdapter(requireContext(), it)
                } else {
                    binding.defensiveHalfDamageOverview.noTypesTextView.visibility = View.VISIBLE
                }
            }
        )
        typeViewModel.defensiveDoubleDamageTypes.observe(
            this as LifecycleOwner,
            {
                if(it.isNotEmpty()) {
                    binding.defensiveDoubleDamageOverview.damageRecyclerView.adapter =
                        PokemonTypeAdapter(requireContext(), it)
                } else {
                    binding.defensiveDoubleDamageOverview.noTypesTextView.visibility = View.VISIBLE
                }
            }
        )
        typeViewModel.defensiveNoDamageTypes.observe(
            this as LifecycleOwner,
            {
                if(it.isNotEmpty()) {
                    binding.defensiveNoDamageOverview.damageRecyclerView.adapter =
                        PokemonTypeAdapter(requireContext(), it)
                } else {
                    binding.defensiveNoDamageOverview.noTypesTextView.visibility = View.VISIBLE
                }
            }
        )

        typeViewModel.getOffensiveDoubleDamageTypes(
            pokemonType.damage_relations.double_damage_to.map { it.url }
        )
        typeViewModel.getOffensiveHalfDamageTypes(
            pokemonType.damage_relations.half_damage_to.map { it.url }
        )
        typeViewModel.getOffensiveNoDamageTypes(
            pokemonType.damage_relations.no_damage_to.map { it.url }
        )
        typeViewModel.getDefensiveHalfDamageTypes(
            pokemonType.damage_relations.half_damage_from.map { it.url }
        )
        typeViewModel.getDefensiveDoubleTypes(
            pokemonType.damage_relations.double_damage_from.map { it.url }
        )
        typeViewModel.getDefensiveNoDamageTypes(
            pokemonType.damage_relations.no_damage_from.map { it.url }
        )
        return view
    }
}