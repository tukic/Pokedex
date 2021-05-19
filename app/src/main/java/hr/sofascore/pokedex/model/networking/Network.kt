package hr.sofascore.pokedex.model.networking

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Network {
    private val service: PokemonService
    private val baseUrl = "https://pokeapi.co/api/v2"

    init {
        val httpClient = OkHttpClient.Builder()
        val retrofit = Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create()).client(httpClient.build()).build()
        service = retrofit.create(PokemonService::class.java)
    }

    fun getService() = service
}