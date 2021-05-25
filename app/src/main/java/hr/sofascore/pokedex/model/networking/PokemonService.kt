package hr.sofascore.pokedex.model.networking

import hr.sofascore.pokedex.model.shared.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

interface PokemonService {

    @GET("pokemon/{name}")
    suspend fun getPokemonByName(@Path("name") name: String): Response<PokemonResponse>

    @GET("pokemon/{id}")
    suspend fun getPokemonById(@Path("id") id: Int): Response<PokemonResponse>

    @GET
    suspend fun getPokemonByURL(@Url url: String): Response<PokemonResponse>

    @GET
    suspend fun getPagedPokemon(@Url url: String): Response<PokemonList>

    @GET("pokemon-species/{id}")
    suspend fun getSpecies(@Path("id") id: Int): Response<PokemonSpecies>

    @GET
    suspend fun getEvolutionChain(@Url url: String): Response<EvolutionChain>

    @GET
    suspend fun getPokemonsFromType(@Url url: String): Response<PokemonType>
}
