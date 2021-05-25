package hr.sofascore.pokedex.ui.type

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import hr.sofascore.pokedex.databinding.ActivityTypeBinding
import hr.sofascore.pokedex.model.shared.PokemonType
import java.util.*

const val POKEMON_TYPE_EXTRA = "POKEMON_TYPE_EXTRA"

class TypeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTypeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        val pokemonType = intent.getSerializableExtra(POKEMON_TYPE_EXTRA) as PokemonType
        super.onCreate(savedInstanceState)

        binding = ActivityTypeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.title.text = pokemonType.type.name.capitalize(Locale.getDefault())
        binding.appBar.backgroundTintList = getColorStateList(pokemonType.type.getTypeColor())
        binding.tabs.backgroundTintList = getColorStateList(pokemonType.type.getTypeColor())

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        tabs.setupWithViewPager(viewPager)

        binding.backArrowView.setOnClickListener{
            onBackPressed()
        }
    }
}