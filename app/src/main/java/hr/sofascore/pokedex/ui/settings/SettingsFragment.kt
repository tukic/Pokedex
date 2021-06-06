package hr.sofascore.pokedex.ui.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import com.google.android.material.snackbar.Snackbar
import hr.sofascore.pokedex.R
import hr.sofascore.pokedex.databinding.FragmentSettingsBinding
import hr.sofascore.pokedex.databinding.PopoverLayoutBinding
import hr.sofascore.pokedex.ui.settings.about.AboutActivity
import hr.sofascore.pokedex.ui.views.Snackbars.Companion.configNormal
import hr.sofascore.pokedex.util.LanguageHelper
import hr.sofascore.pokedex.viewmodels.LanguageViewModel
import hr.sofascore.pokedex.viewmodels.PokemonViewModel

class SettingsFragment : Fragment() {

    private val languageViewModel: LanguageViewModel by activityViewModels()
    private val pokemonViewModel: PokemonViewModel by activityViewModels()

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
            { language ->
                binding.languageSpinner.adapter = ArrayAdapter(
                    requireContext(),
                    R.layout.spinner_language_item,
                    language.results.map { it.name }.toList()
                )
                var select = 0
                language.results.forEachIndexed { index, result ->
                    if (result.name == LanguageHelper.getPreferredLanguage(requireContext())) {
                        select = index
                    }
                }
                binding.languageSpinner.setSelection(select)
            }
        )
        languageViewModel.getLanguages()

        binding.languageSpinner.adapter = ArrayAdapter(
            requireContext(),
            R.layout.spinner_language_item,
            arrayListOf<String>().apply {
                add(LanguageHelper.getPreferredLanguage(requireContext()))
            }
        )

        binding.aboutBackgroundView.setOnClickListener {
            context?.startActivity(Intent(context, AboutActivity::class.java))
        }

        binding.languageSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    LanguageHelper.setPreferredLanguage(
                        requireContext(),
                        parent?.getItemAtPosition(position) as String
                    )
                }
            }

        binding.clearFavoritesTextView.setOnClickListener {
            val popoverBinding = PopoverLayoutBinding.inflate(inflater, binding.root, false)
            val width = LinearLayout.LayoutParams.MATCH_PARENT
            val height = LinearLayout.LayoutParams.MATCH_PARENT
            val popupWindow = PopupWindow(popoverBinding.root, width, height, true)
            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)

            popoverBinding.cancelTextView.setOnClickListener {
                popupWindow.dismiss()
            }

            popoverBinding.clearTextView.setOnClickListener {
                pokemonViewModel.deleteAllPokemons(requireContext())
                popupWindow.dismiss()
                val snackbar = Snackbar.make(
                    binding.snackbarContainer,
                    getString(R.string.snackbar_favorites_cleared_text),
                    Snackbar.LENGTH_LONG
                ).setAction(" ") {
                    it.visibility = View.GONE
                }
                snackbar.configNormal(requireContext())
                snackbar.show()
            }
        }

        return view
    }
}