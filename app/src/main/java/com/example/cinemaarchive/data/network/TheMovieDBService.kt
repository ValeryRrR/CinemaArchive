package com.example.cinemaarchive.data.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TheMovieDBService {
    @GET("discover/movie")
    fun getListFilms(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") pageIndex: Int
    ): Call<ResponseDataClass>
}

