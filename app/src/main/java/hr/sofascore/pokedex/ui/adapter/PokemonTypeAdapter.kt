package hr.sofascore.pokedex.ui.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hr.sofascore.pokedex.R
import hr.sofascore.pokedex.databinding.PokemonTypeLayoutBinding
import hr.sofascore.pokedex.model.shared.PokemonType
import hr.sofascore.pokedex.ui.type.POKEMON_TYPE_EXTRA
import hr.sofascore.pokedex.ui.type.TypeActivity
import java.util.*

class PokemonTypeAdapter (
    val context: Context,
    val types: List<PokemonType>
) : RecyclerView.Adapter<PokemonTypeAdapter.ViewHolder>() {

    class ViewHolder(view: View, viewGroup: ViewGroup) : RecyclerView.ViewHolder(view) {
        val binding = PokemonTypeLayoutBinding.bind(view)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.pokemon_type_layout, viewGroup, false)

        return ViewHolder(view, viewGroup)
    }


    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val type = types[position]
        viewHolder.binding.root.backgroundTintList = context.getColorStateList(type.getTypeColor())
        viewHolder.binding.pokemonTypeTextView.text = type.name.capitalize(Locale.getDefault())

        viewHolder.binding.root.setOnClickListener {
            val intent = Intent(context, TypeActivity::class.java).apply {
                putExtra(POKEMON_TYPE_EXTRA, type)
            }
            context.startActivity(intent)
        }
    }

    companion object {
        class SpacesItemDecoration(private val spaceSize: Int) : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                outRect.right = spaceSize
                outRect.bottom = spaceSize
            }

        }
    }

    override fun getItemCount() = types.size
}