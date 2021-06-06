package hr.sofascore.pokedex.ui.type

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import hr.sofascore.pokedex.databinding.FragmentDamageOverviewBinding
import hr.sofascore.pokedex.model.shared.PokemonType
import hr.sofascore.pokedex.ui.adapter.PokemonTypeAdapter
import hr.sofascore.pokedex.ui.views.Snackbars.Companion.configError
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

        typeViewModel.error.observe(
            this as LifecycleOwner,
            { message ->
                val snackbar = Snackbar.make(
                    binding.snackbarContainer,
                    message,
                    Snackbar.LENGTH_LONG
                ).setAction(" ") {
                    it.visibility = View.GONE
                }
                snackbar.configError(requireContext())
                snackbar.show()
            }
        )

        typeViewModel.offensiveDoubleDamageTypes.observe(
            this as LifecycleOwner,
            { types ->
                binding.offenseDoubleDamageOverview.setRecycler(requireContext(), types)
            }
        )
        typeViewModel.offensiveHalfDamageTypes.observe(
            this as LifecycleOwner,
            { types ->
                binding.offenseHalfDamageOverview.setRecycler(requireContext(), types)
            }
        )
        typeViewModel.offensiveNoDamageTypes.observe(
            this as LifecycleOwner,
            { types ->
                binding.offenseNoDamageOverview.setRecycler(requireContext(), types)
            }
        )
        typeViewModel.defensiveHalfDamageTypes.observe(
            this as LifecycleOwner,
            { types ->
                binding.defensiveHalfDamageOverview.setRecycler(requireContext(), types)
            }
        )
        typeViewModel.defensiveDoubleDamageTypes.observe(
            this as LifecycleOwner,
            { types ->
                binding.defensiveDoubleDamageOverview.setRecycler(requireContext(), types)
            }
        )
        typeViewModel.defensiveNoDamageTypes.observe(
            this as LifecycleOwner,
            { types ->
                binding.defensiveNoDamageOverview.setRecycler(requireContext(), types)
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