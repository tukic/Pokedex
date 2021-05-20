package hr.sofascore.pokedex.model.networking

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val initialPokemonURL = "https://pokeapi.co/api/v2/pokemon"

class Network {
    private val service: PokemonService
    private val baseUrl = "https://pokeapi.co/api/v2/"

    init {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC)
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)
        val retrofit = Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create()).client(httpClient.build()).build()
        service = retrofit.create(PokemonService::class.java)
    }

    fun getService() = service
}