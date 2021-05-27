package hr.sofascore.pokedex.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hr.sofascore.pokedex.R
import hr.sofascore.pokedex.databinding.DamageOverviewItemBinding
import kotlin.math.roundToInt

class DamageOverviewItem(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    private val binding: DamageOverviewItemBinding
    val damageRecyclerView: RecyclerView
    val noTypesTextView: TextView

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
            binding.damageRecycler.background =
                getDrawable(R.styleable.DamageOverviewItem_item_background)
            binding.damageRecycler.backgroundTintList =
                getColorStateList(R.styleable.DamageOverviewItem_background_color)
            val alpha = ((getInt(R.styleable.DamageOverviewItem_background_opacity, 0)
                .toFloat()  / 100) * 255).roundToInt()
            binding.damageRecycler.background.alpha = alpha
            damageRecyclerView = binding.damageRecycler
            noTypesTextView = binding.noTypesLabelTextView
        }
    }
}