package hr.sofascore.pokedex.ui.type

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import hr.sofascore.pokedex.R

private val TAB_TITLES = arrayOf(
    R.string.damage_overview,
    R.string.moves,
    R.string.pokemon
)

class SectionsPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        when(position) {
            0 -> return DamageOverview()
            1 -> return Moves()
            2 -> return Pokemons()
        }
        return DamageOverview()
    }

    override fun getPageTitle(position: Int): CharSequence {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        return 3
    }
}