package hr.sofascore.pokedex.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import hr.sofascore.pokedex.R
import hr.sofascore.pokedex.databinding.FragmentSettingsBinding
import hr.sofascore.pokedex.viewmodels.LanguageViewModel


class SettingsFragment : Fragment() {

    private val languageViewModel: LanguageViewModel by activityViewModels()

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val view = binding.root

        languageViewModel.languages.observe(
            this as LifecycleOwner,
            {
                binding.languageSpinner.adapter = ArrayAdapter(
                    requireContext(),
                    R.layout.spinner_language_item,
                    it.results.map { it.name }.toList()
                )
            }
        )
        languageViewModel.getLanguages()

        return view
    }
}