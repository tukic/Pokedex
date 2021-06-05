package hr.sofascore.pokedex.ui.type

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import hr.sofascore.pokedex.databinding.FragmentDamageOverviewBinding
import hr.sofascore.pokedex.model.shared.PokemonType
import hr.sofascore.pokedex.ui.adapter.PokemonTypeAdapter
import hr.sofascore.pokedex.viewmodels.TypeViewModel
import kotlin.math.ceil

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

        typeViewModel.offensiveDoubleDamageTypes.observe(
            this as LifecycleOwner,
            {
                if (it.isNotEmpty()) {
                    binding.offenseDoubleDamageOverview.damageRecyclerView.adapter =
                        PokemonTypeAdapter(requireContext(), it)
                    binding.offenseDoubleDamageOverview.damageRecyclerView.layoutManager =
                        StaggeredGridLayoutManager(
                            ceil(it.size.toFloat() / 4).toInt(),
                            StaggeredGridLayoutManager.HORIZONTAL
                        )
                } else {
                    binding.offenseDoubleDamageOverview.noTypesTextView.visibility = View.VISIBLE
                }
                binding.offenseDoubleDamageOverview.removeProgressBar()
            }
        )
        typeViewModel.offensiveHalfDamageTypes.observe(
            this as LifecycleOwner,
            {
                if (it.isNotEmpty()) {
                    binding.offenseHalfDamageOverview.damageRecyclerView.layoutManager =
                        StaggeredGridLayoutManager(
                            ceil(it.size.toFloat() / 4).toInt(),
                            StaggeredGridLayoutManager.HORIZONTAL
                        )
                    binding.offenseHalfDamageOverview.damageRecyclerView.adapter =
                        PokemonTypeAdapter(requireContext(), it)
                } else {
                    binding.offenseHalfDamageOverview.noTypesTextView.visibility = View.VISIBLE
                }
                binding.offenseHalfDamageOverview.removeProgressBar()
            }
        )
        typeViewModel.offensiveNoDamageTypes.observe(
            this as LifecycleOwner,
            {
                if (it.isNotEmpty()) {
                    binding.offenseNoDamageOverview.damageRecyclerView.adapter =
                        PokemonTypeAdapter(requireContext(), it)
                    binding.offenseNoDamageOverview.damageRecyclerView.layoutManager =
                        StaggeredGridLayoutManager(
                            ceil(it.size.toFloat() / 4).toInt(),
                            StaggeredGridLayoutManager.HORIZONTAL
                        )
                } else {
                    binding.offenseNoDamageOverview.noTypesTextView.visibility = View.VISIBLE
                }
                binding.offenseNoDamageOverview.removeProgressBar()
            }
        )
        typeViewModel.defensiveHalfDamageTypes.observe(
            this as LifecycleOwner,
            {
                if (it.isNotEmpty()) {
                    binding.defensiveHalfDamageOverview.damageRecyclerView.adapter =
                        PokemonTypeAdapter(requireContext(), it)
                    binding.defensiveHalfDamageOverview.damageRecyclerView.layoutManager =
                        StaggeredGridLayoutManager(
                            ceil(it.size.toFloat() / 4).toInt(),
                            StaggeredGridLayoutManager.HORIZONTAL
                        )
                } else {
                    binding.defensiveHalfDamageOverview.noTypesTextView.visibility = View.VISIBLE
                }
                binding.defensiveHalfDamageOverview.removeProgressBar()
            }
        )
        typeViewModel.defensiveDoubleDamageTypes.observe(
            this as LifecycleOwner,
            {
                if (it.isNotEmpty()) {
                    binding.defensiveDoubleDamageOverview.damageRecyclerView.adapter =
                        PokemonTypeAdapter(requireContext(), it)
                    binding.defensiveDoubleDamageOverview.damageRecyclerView.layoutManager =
                        StaggeredGridLayoutManager(
                            ceil(it.size.toFloat() / 4).toInt(),
                            StaggeredGridLayoutManager.HORIZONTAL
                        )
                } else {
                    binding.defensiveDoubleDamageOverview.noTypesTextView.visibility = View.VISIBLE
                }
                binding.defensiveDoubleDamageOverview.removeProgressBar()
            }
        )
        typeViewModel.defensiveNoDamageTypes.observe(
            this as LifecycleOwner,
            {
                if (it.isNotEmpty()) {
                    binding.defensiveNoDamageOverview.damageRecyclerView.adapter =
                        PokemonTypeAdapter(requireContext(), it)
                    binding.defensiveNoDamageOverview.damageRecyclerView.layoutManager =
                        StaggeredGridLayoutManager(
                            ceil(it.size.toFloat() / 4).toInt(),
                            StaggeredGridLayoutManager.HORIZONTAL
                        )
                } else {
                    binding.defensiveNoDamageOverview.noTypesTextView.visibility = View.VISIBLE
                }
                binding.defensiveNoDamageOverview.removeProgressBar()
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