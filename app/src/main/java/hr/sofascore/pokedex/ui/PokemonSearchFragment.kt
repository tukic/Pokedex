package hr.sofascore.pokedex.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import hr.sofascore.pokedex.R
import hr.sofascore.pokedex.databinding.FragmentPokemonSearchBinding


class PokemonSearchFragment : Fragment() {

    private var _binding: FragmentPokemonSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)

        _binding = FragmentPokemonSearchBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

}