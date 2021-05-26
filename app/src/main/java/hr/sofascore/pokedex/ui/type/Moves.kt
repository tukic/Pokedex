package hr.sofascore.pokedex.ui.type

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableRow
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import hr.sofascore.pokedex.databinding.FragmentMovesBinding
import hr.sofascore.pokedex.model.shared.PokemonType
import hr.sofascore.pokedex.viewmodels.MoveViewModel

class Moves(val pokemonType: PokemonType) : Fragment() {

    val moveViewModel by activityViewModels<MoveViewModel>()

    private var _binding: FragmentMovesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)

        _binding = FragmentMovesBinding.inflate(inflater, container, false)
        val view = binding.root

        moveViewModel.move.observe(
            this as LifecycleOwner,
            {
                it.forEach {
                    val tr = TableRow(requireContext())
                    tr.addView(TextView(context).apply {
                        text = it.generation.getRomanNumberGen()
                    })
                    tr.addView(TextView(context).apply {
                        text = it.name
                    })
                    it.damage_class?.let {
                        tr.addView(TextView(context).apply {
                            text = it.name
                        })
                    }
                    tr.addView(TextView(context).apply {
                        text = it.power.toString()
                    })
                    tr.addView(TextView(context).apply {
                        text = it.pp.toString()
                    })
                    binding.table.addView(tr)
                }
            }
        )
        moveViewModel.getMoves(pokemonType.moves.map { it.url })


        return view
    }
}