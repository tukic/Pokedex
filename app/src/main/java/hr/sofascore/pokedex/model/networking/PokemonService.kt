package hr.sofascore.pokedex.model.networking

import hr.sofascore.pokedex.model.shared.PokemonResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface PokemonService {

    @GET("pokemon/{name}")
    suspend fun getPokemon(@Path("name") name: String): Response<List<PokemonResponse>>

}