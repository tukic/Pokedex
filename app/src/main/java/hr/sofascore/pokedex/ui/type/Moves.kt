package hr.sofascore.pokedex.ui.type

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableRow
import android.widget.TextView
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import hr.sofascore.pokedex.R
import hr.sofascore.pokedex.databinding.FragmentMovesBinding
import hr.sofascore.pokedex.model.shared.PokemonMove
import hr.sofascore.pokedex.model.shared.PokemonType
import hr.sofascore.pokedex.viewmodels.MoveViewModel
import kotlin.math.roundToInt

class Moves(val pokemonType: PokemonType) : Fragment() {

    private val moveViewModel by activityViewModels<MoveViewModel>()

    private var _binding: FragmentMovesBinding? = null
    private val binding get() = _binding!!

    private var genAscending = true
    private var moveAscending = true
    private var categoryAscending = true
    private var powerAscending = true
    private var ppAscending = true

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
                addRowsToTable(it)
            }
        )
        moveViewModel.getMoves(pokemonType.moves.map { it.url })

        binding.genCell.setOnClickListener {
            if (genAscending) {
                moveViewModel.move.value?.sortedByDescending { it.generation.name }?.let {
                    addRowsToTable(it)
                }
                genAscending = false
            } else {
                moveViewModel.move.value?.sortedBy { it.generation.name }?.let {
                    addRowsToTable(it)
                }
                genAscending = true
            }
        }
        binding.moveCell.setOnClickListener {
            if (moveAscending) {
                moveViewModel.move.value?.sortedByDescending { it.name }?.let {
                    addRowsToTable(it)
                }
                moveAscending = false
            } else {
                moveViewModel.move.value?.sortedBy { it.name }?.let {
                    addRowsToTable(it)
                }
                moveAscending = true
            }
        }
        binding.categoryCell.setOnClickListener {
            if (categoryAscending) {
                moveViewModel.move.value?.sortedByDescending { it.damage_class?.name }?.let {
                    addRowsToTable(it)
                }
                categoryAscending = false
            } else {
                moveViewModel.move.value?.sortedBy { it.damage_class?.name }?.let {
                    addRowsToTable(it)
                }
                categoryAscending = true
            }
        }
        binding.powerCell.setOnClickListener {
            if (powerAscending) {
                moveViewModel.move.value?.sortedByDescending { it.power }?.let {
                    addRowsToTable(it)
                }
                powerAscending = false
            } else {
                moveViewModel.move.value?.sortedBy { it.power }?.let {
                    addRowsToTable(it)
                }
                powerAscending = true
            }
        }
        binding.ppCell.setOnClickListener {
            if (ppAscending) {
                moveViewModel.move.value?.sortedByDescending { it.pp }?.let {
                    addRowsToTable(it)
                }
                ppAscending = false
            } else {
                moveViewModel.move.value?.sortedBy { it.pp }?.let {
                    addRowsToTable(it)
                }
                ppAscending = true
            }
        }
        return view
    }

    fun addRowsToTable(pokemonMove: List<PokemonMove>) {
        while (binding.table.childCount > 1) {
            binding.table.removeViewAt(binding.table.childCount - 1)
        }
        pokemonMove.forEach {
            val row = TableRow(requireContext())
            row.addView(TextView(context, null, R.style.AssistiveDarkCenter).apply {
                text = it.generation.getRomanNumberGen()
                gravity = Gravity.CENTER
                setPadding(resources.getDimensionPixelSize(R.dimen.moves_table_column_margin))
                background = context.getDrawable(it.generation.getColor())
                background.alpha = (0.3 * 255).roundToInt()
            })
            row.addView(TextView(context, null, R.style.AssistiveDarkCenter).apply {
                text = it.name
                gravity = Gravity.CENTER
                //setPadding(resources.getDimensionPixelSize(R.dimen.moves_table_column_margin))
            })

            row.addView(TextView(context, null, R.style.AssistiveDarkCenter).apply {
                text = it.damage_class?.name ?: "-"
                gravity = Gravity.CENTER
                setPadding(resources.getDimensionPixelSize(R.dimen.moves_table_column_margin))
                it.damage_class?.let{
                    background = context.getDrawable(it.getDamageClassColor())
                    background.alpha = (0.3 * 255).roundToInt()
                }
            })

            row.addView(TextView(context, null, R.style.AssistiveDarkCenter).apply {
                text = it.power.let {
                    if (it != 0) it.toString()
                    else "-"
                }
                gravity = Gravity.CENTER
                setPadding(resources.getDimensionPixelSize(R.dimen.moves_table_column_margin))
            })
            row.addView(TextView(context, null, R.style.AssistiveDarkCenter).apply {
                text = it.pp.let {
                    if (it != 0) it.toString()
                    else "-"
                }
                gravity = Gravity.CENTER
                setPadding(resources.getDimensionPixelSize(R.dimen.moves_table_column_margin))
                //setBackgroundColor(context.getColor(R.color.cold_gray))
                //background.alpha = (0.1 * 255).roundToInt()
            })
            binding.table.addView(row)
        }
    }
}