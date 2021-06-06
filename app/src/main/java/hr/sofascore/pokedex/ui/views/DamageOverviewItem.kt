package hr.sofascore.pokedex.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import hr.sofascore.pokedex.R
import hr.sofascore.pokedex.databinding.DamageOverviewItemBinding
import hr.sofascore.pokedex.model.shared.PokemonType
import hr.sofascore.pokedex.ui.adapter.PokemonTypeAdapter
import kotlin.math.ceil
import kotlin.math.roundToInt

class DamageOverviewItem(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    private val binding: DamageOverviewItemBinding

    init {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.damage_overview_item, null, false)

        binding = DamageOverviewItemBinding.bind(view)
        addView(binding.root)

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.DamageOverviewItem,
            0, 0
        ).apply {
            binding.powerTextView.text = getString(R.styleable.DamageOverviewItem_power_amount)
            binding.powerTextView.setTextColor(
                getColor(
                    R.styleable.DamageOverviewItem_background_color,
                    0
                )
            )
            binding.recyclerBackgroundView.background =
                getDrawable(R.styleable.DamageOverviewItem_item_background)
            binding.recyclerBackgroundView.backgroundTintList =
                getColorStateList(R.styleable.DamageOverviewItem_background_color)
            val alpha = ((getInt(R.styleable.DamageOverviewItem_background_opacity, 0)
                .toFloat()  / 100) * 255).roundToInt()
            binding.recyclerLeftBorderView.backgroundTintList =
                getColorStateList(R.styleable.DamageOverviewItem_background_color)
            binding.recyclerBackgroundView.background.alpha = alpha
        }
    }

    fun setRecycler(context: Context, types: List<PokemonType>) {
        if (types.isNotEmpty()) {
            binding.damageRecycler.adapter =
                PokemonTypeAdapter(context, types)
            binding.damageRecycler.layoutManager =
                StaggeredGridLayoutManager(
                    ceil(types.size.toFloat() / 4).toInt(),
                    StaggeredGridLayoutManager.HORIZONTAL
                )
        } else {
            binding.noTypesLabelTextView.visibility = View.VISIBLE
        }
        removeProgressBar()
    }

    private fun removeProgressBar() {
        binding.progressBar.visibility = View.GONE
    }
}