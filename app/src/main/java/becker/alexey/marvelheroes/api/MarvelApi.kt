package becker.alexey.marvelheroes.api

import becker.alexey.marvelheroes.data.heroresponse.HeroesResponse
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface MarvelApi {

    @GET("v1/public/characters")
    suspend fun getNewHeroes(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int?,
        @Query("apikey") apiKey: String,
        @Query("hash") hash: String,
        @Query("ts") ts: String
    ): HeroesResponse

    companion object {
        private const val BASE_URL = "https://gateway.marvel.com/"
        fun create(): MarvelApi {
            //    val logger = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                //         .addInterceptor(logger)
                .build()
            return Retrofit.Builder()
                .baseUrl(HttpUrl.parse(BASE_URL)!!)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MarvelApi::class.java)
        }
    }
}