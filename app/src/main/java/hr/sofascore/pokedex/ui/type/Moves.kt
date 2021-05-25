package hr.sofascore.pokedex.ui.type

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import hr.sofascore.pokedex.databinding.FragmentMovesBinding

class Moves : Fragment() {
    private var _binding: FragmentMovesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)

        _binding = FragmentMovesBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }
}