package hr.sofascore.pokedex.ui.favorites

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hr.sofascore.pokedex.databinding.FragmentFavoritesBinding
import hr.sofascore.pokedex.model.shared.PokemonResponse
import hr.sofascore.pokedex.ui.adapter.FavoritePokemonAdapter
import hr.sofascore.pokedex.ui.adapter.ReorderFavoritesAdapter
import hr.sofascore.pokedex.ui.adapter.ReorderingFragment
import hr.sofascore.pokedex.ui.adapter.StartPokemonActivityListener
import hr.sofascore.pokedex.ui.pokemon.POKEMON_EXTRA
import hr.sofascore.pokedex.ui.pokemon.PokemonActivity
import hr.sofascore.pokedex.ui.views.FavouritePokemonListener
import hr.sofascore.pokedex.viewmodels.PokemonViewModel

class FavoritesFragment : Fragment(), StartPokemonActivityListener, FavouritePokemonListener,
    ReorderingFragment {

    val pokemonViewModel by activityViewModels<PokemonViewModel>()

    private var _binding: FragmentFavoritesBinding? = null
    val binding get() = _binding!!

    private val itemTouchHelper by lazy {
        val simpleItemTouchCallback =
            object : ItemTouchHelper.SimpleCallback(UP or DOWN or START or END, 0) {
                override fun onMove(recyclerView: RecyclerView,
                                    viewHolder: RecyclerView.ViewHolder,
                                    target: RecyclerView.ViewHolder): Boolean {
                    val adapter = recyclerView.adapter as ReorderFavoritesAdapter
                    val from = viewHolder.adapterPosition
                    val to = target.adapterPosition
                    adapter.moveItem(from, to)
                    adapter.notifyItemMoved(from, to)

                    return true
                }
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                }
            }

        ItemTouchHelper(simpleItemTouchCallback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.favoritesRecyclerView.layoutManager = LinearLayoutManager(context)

        binding.editFavoritesReorderingView.setOnClickListener{
            pokemonViewModel.favouritePokemon.value?.let {
                binding.editFavoritesReorderingView.visibility = View.GONE
                binding.favoritesRecyclerView.adapter =
                    ReorderFavoritesAdapter(
                        requireContext(),
                        it.sortedBy { it.favoriteIndex }.toMutableList(), this, this, this
                    )
                itemTouchHelper.attachToRecyclerView(binding.favoritesRecyclerView)
                binding.doneFavoritesReorderingView.visibility = View.VISIBLE
            }
        }
        binding.doneFavoritesReorderingView.setOnClickListener {
            pokemonViewModel.favouritePokemon.value?.let {
                binding.doneFavoritesReorderingView.visibility = View.GONE
                binding.favoritesRecyclerView.adapter =
                    FavoritePokemonAdapter(requireContext(), it.sortedBy { it.favoriteIndex }, this, this)
                itemTouchHelper.attachToRecyclerView(null)
                binding.editFavoritesReorderingView.visibility = View.VISIBLE
            }
        }

        pokemonViewModel.favouritePokemon.observe(
            this as LifecycleOwner,
            {
                binding.favoritesRecyclerView.adapter =
                    FavoritePokemonAdapter(requireContext(), it, this, this)
            }
        )

        return view
    }

    override fun startActivity(pokemon: PokemonResponse) {
        val intent = Intent(context, PokemonActivity::class.java).apply {
            putExtra(POKEMON_EXTRA, pokemon)
        }
        context?.startActivity(intent)
    }

    override fun favouritePokemonAdded(pokemon: PokemonResponse) {
        pokemonViewModel.insertFavouritePokemon(requireContext(), pokemon)
    }

    override fun favouritePokemonRemoved(pokemon: PokemonResponse) {
        pokemonViewModel.deleteFavouritePokemon(requireContext(), pokemon)
    }

    override fun updateFavouritePokemon(): List<PokemonResponse> {
        return pokemonViewModel.favouritePokemon.value as List<PokemonResponse>
    }

    override fun onStart() {
        pokemonViewModel.getFavouritePokemonSortedByByFavoriteIndex(requireContext())
        super.onStart()
    }

    override fun startDragging(viewHolder: RecyclerView.ViewHolder) {
        itemTouchHelper.startDrag(viewHolder)
    }

    override fun updateReorderedPokemons(pokemons: List<PokemonResponse>) {
        pokemonViewModel.updatePokemon(requireContext(), pokemons)
    }
}