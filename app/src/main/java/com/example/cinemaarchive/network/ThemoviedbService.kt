package com.example.cinemaarchive.network


import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


private const val BASE_URL = "https://api.themoviedb.org/3/"
const val API_KEY = "4ad6223423726930ca824e38f157d79e"
const val ENG_LANG = "en-US"


interface ThemoviedbService {
    @GET("discover/movie")
    fun listFilms(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") pageIndex: Int
    ): Call<ResponseDataClass>
}

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

object FilmApi {
    val retrofitService: ThemoviedbService by lazy {
        retrofit.create(ThemoviedbService::class.java)
    }
}