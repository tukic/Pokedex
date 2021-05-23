package hr.sofascore.pokedex.ui.views

import hr.sofascore.pokedex.model.shared.PokemonResponse

interface FavouritePokemonListener {

    fun favouritePokemonAdded(pokemon: PokemonResponse)

    fun favouritePokemonRemoved(pokemon: PokemonResponse)

    fun updateFavouritePokemon(): List<PokemonResponse>
}