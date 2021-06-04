package hr.sofascore.pokedex.model.networking

import hr.sofascore.pokedex.model.shared.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface PokemonService {

    @GET("pokemon/{name}")
    suspend fun getPokemonByName(@Path("name") name: String): Response<PokemonResponse>

    @GET("pokemon/{id}")
    suspend fun getPokemonById(@Path("id") id: Int): Response<PokemonResponse>

    @GET
    suspend fun getPokemonByURL(@Url url: String): Response<PokemonResponse>

    @GET("pokemon")
    suspend fun getPagedPokemons(@Query("limit") limit: Int): Response<PokemonList>

    @GET
    suspend fun getPagedPokemonByURL(@Url url: String): Response<PokemonList>

    @GET("pokemon")
    suspend fun getPagedPokemonByURL(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int? = null
    ): Response<PokemonList>

    @GET("pokemon-species/{id}")
    suspend fun getSpecies(@Path("id") id: Int): Response<PokemonSpecies>

    @GET
    suspend fun getEvolutionChain(@Url url: String): Response<EvolutionChain>

    @GET
    suspend fun getPokemonType(@Url url: String): Response<PokemonType>

    @GET
    suspend fun getTypesByURL(@Url url: String): Response<PokemonType>

    @GET
    suspend fun getStatByURL(@Url url: String): Response<Stat>

    @GET
    suspend fun getPokeathlonStatByURL(@Url url: String): Response<PokeathlonStat>

    @GET
    suspend fun getAbilityByURL(@Url url: String): Response<AbilityResponse>

    @GET
    suspend fun getPokemonMove(@Url url: String): Response<PokemonMove>

    @GET("language")
    suspend fun getLanguages(): Response<Language>

    @GET("type")
    suspend fun getPagedTypes(@Query("limit") limit: Int): Response<TypeList>

    @GET
    suspend fun getPokemonMoveDamageClass(@Url url: String): Response<PokemonDamageClass>
}
