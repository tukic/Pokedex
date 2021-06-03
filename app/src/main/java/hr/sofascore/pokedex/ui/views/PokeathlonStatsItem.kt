package hr.sofascore.pokedex.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import hr.sofascore.pokedex.R
import hr.sofascore.pokedex.databinding.PokeathlonStatsItemBinding

class PokeathlonStatsItem(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    private val binding: PokeathlonStatsItemBinding

    init {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.pokeathlon_stats_item, null, false)

        binding = PokeathlonStatsItemBinding.bind(view)
        addView(binding.root)

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.PokeathlonStatsItem,
            0, 0
        ).apply {
            binding.pokeathlonTitleTextView.text =
                getString(R.styleable.PokeathlonStatsItem_stat_name)
        }
    }

    fun addStars(filledStars: Int, emptyStars: Int, color: Int) {
        binding.pokeathlonStatAmountTextView.text = "$filledStars/${filledStars + emptyStars}"
        val starDimen = resources.getDimensionPixelSize(R.dimen.pokeathlon_star_size)
        for (i: Int in 1..filledStars) {
            val starView = View(context).apply {
                background = context.getDrawable(R.drawable.ic_star_1)?.apply {
                    setTint(context.getColor(color))
                }
                layoutParams = LayoutParams(starDimen, starDimen)
            }
            binding.pokeathlonStarsLinearLayout.addView(starView)
        }
        for (i: Int in 1..emptyStars) {
            val starView = View(context).apply {
                background = context.getDrawable(R.drawable.ic_star_0)?.apply {
                    setTint(context.getColor(color))
                }
                layoutParams = LayoutParams(starDimen, starDimen)
            }
            binding.pokeathlonStarsLinearLayout.addView(starView)
        }
    }

    fun addTotalStars(filledStars: Int, emptyStars: Int, total: Int, maxTotal: Int, color: Int) {
        binding.pokeathlonStatAmountTextView.text = "$total/${maxTotal}"
        val starDimen = resources.getDimensionPixelSize(R.dimen.pokeathlon_star_size)
        for (i: Int in 1..filledStars) {
            val starView = View(context).apply {
                background = context.getDrawable(R.drawable.ic_star_1)?.apply {
                    setTint(context.getColor(color))
                }
                layoutParams = LayoutParams(starDimen, starDimen)
            }
            binding.pokeathlonStarsLinearLayout.addView(starView)
        }
        for (i: Int in 1..emptyStars) {
            val starView = View(context).apply {
                background = context.getDrawable(R.drawable.ic_star_0)?.apply {
                    setTint(context.getColor(color))
                }
                layoutParams = LayoutParams(starDimen, starDimen)
            }
            binding.pokeathlonStarsLinearLayout.addView(starView)
        }
    }
}