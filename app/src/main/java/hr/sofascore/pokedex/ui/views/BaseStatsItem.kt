package hr.sofascore.pokedex.ui.views

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import hr.sofascore.pokedex.R
import hr.sofascore.pokedex.databinding.BaseStatsBinding
import hr.sofascore.pokedex.databinding.PokemonItemBinding

class BaseStatsItem(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    private val binding: BaseStatsBinding

    var statsProgressBar: ProgressBar
    var baseStatsValueTextView: TextView
    var baseStatsLabelTextView: TextView

    init {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.base_stats, null, false)

        binding = BaseStatsBinding.bind(view)
        addView(binding.root)

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.BaseStatsItem,
            0, 0
        ).apply {
            baseStatsValueTextView = binding.baseStatsValueTextView
            statsProgressBar = binding.baseStatsValueProgressBar
            baseStatsLabelTextView = binding.baseStatsLabelTextView
            //binding.baseStatsLabelTextView.text = getString(R.styleable.BaseStatsItem_base_stats_label)
            binding.baseStatsValueProgressBar.progressTintList = ColorStateList.valueOf(
                getColor(R.styleable.BaseStatsItem_stats_bar_color, Color.BLACK)
            )
        }
    }

}