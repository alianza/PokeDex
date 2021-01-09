package com.example.pokedex.model.data.pokeApi

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PokeApi {
    companion object {
        private const val baseUrl = "https://pokeapi.co/api/v2/"

        fun createApi(): PokeApiService {
            // Create an OkHttpClient to be able to make a log of the network traffic
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
                .build()

            // Create the Retrofit instance
            val pokeApi = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            // Return the Retrofit PokesApiService
            return pokeApi.create(PokeApiService::class.java)
        }
    }
}
