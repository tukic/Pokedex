package hr.sofascore.pokedex.ui.adapter

import hr.sofascore.pokedex.model.shared.PokemonResponse

interface StartPokemonActivityListener {
    fun startActivity(pokemon: PokemonResponse)
}