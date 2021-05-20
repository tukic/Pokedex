package hr.sofascore.pokedex.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import coil.api.load
import hr.sofascore.pokedex.databinding.FragmentPokemonSearchBinding
import hr.sofascore.pokedex.viewmodels.PokemonViewModel
import java.util.*


class PokemonSearchFragment : Fragment() {

    val pokemonViewModel: PokemonViewModel by activityViewModels()

    private var _binding: FragmentPokemonSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)

        _binding = FragmentPokemonSearchBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.searchIcon.setOnClickListener {
            pokemonViewModel.getPokemon(
                binding.searchPokemonTextView.text.toString()
            )
        }

        pokemonViewModel.pokemon.observe(
            this as LifecycleOwner,
            {
                binding.pokemonItem.pokemonImageView.load(pokemonImageURL(it.id))
                binding.pokemonItem.pokemonNameTextView.text = it.name.capitalize(Locale.ROOT)
                binding.pokemonItem.pokemonNumTextView.text = "%03d".format(it.id)
            }
        )

        return view
    }
}

fun pokemonImageURL(id: Int) = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${id}.png"