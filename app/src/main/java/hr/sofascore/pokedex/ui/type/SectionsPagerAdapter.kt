package hr.sofascore.pokedex.ui.type

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import hr.sofascore.pokedex.R
import hr.sofascore.pokedex.model.shared.PokemonType
import hr.sofascore.pokedex.model.shared.PokemonTypeInfo

private val TAB_TITLES = arrayOf(
    R.string.damage_overview,
    R.string.moves,
    R.string.pokemon
)

class SectionsPagerAdapter(
    private val context: Context,
    fm: FragmentManager,
    val pokemonType: PokemonType
) :
    FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> return DamageOverview(pokemonType)
            1 -> return Moves()
            2 -> return Pokemons(pokemonType)
        }
        return DamageOverview(pokemonType)
    }

    override fun getPageTitle(position: Int): CharSequence {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        return 3
    }
}