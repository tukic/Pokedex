package hr.sofascore.pokedex.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hr.sofascore.pokedex.R
import hr.sofascore.pokedex.databinding.PokemonTypeLayoutBinding
import hr.sofascore.pokedex.model.shared.PokemonType
import hr.sofascore.pokedex.ui.type.POKEMON_TYPE_EXTRA
import hr.sofascore.pokedex.ui.type.PokemonTypeActivity
import java.util.*

class PokemonTypeAdapter (
    val context: Context,
    val types: List<PokemonType>
) : RecyclerView.Adapter<PokemonTypeAdapter.ViewHolder>() {

    class ViewHolder(view: View, viewGroup: ViewGroup) : RecyclerView.ViewHolder(view) {
        val binding = PokemonTypeLayoutBinding.bind(view)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(context)
            .inflate(R.layout.pokemon_type_layout, viewGroup, false)

        return ViewHolder(view, viewGroup)
    }


    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val type = types[position]
        viewHolder.binding.root.backgroundTintList = context.getColorStateList(type.type.getTypeColor())
        viewHolder.binding.pokemonTypeTextView.text = type.type.name.capitalize(Locale.getDefault())

        viewHolder.binding.root.setOnClickListener {
            val intent = Intent(context, PokemonTypeActivity::class.java).apply {
                putExtra(POKEMON_TYPE_EXTRA, type)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = types.size
}