package com.example.cinemaarchive.data.network

import com.example.cinemaarchive.domain.entity.Film
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TheMovieDBApi {
    @GET("discover/movie")
    fun getListFilms(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") pageIndex: Int
    ): Call<ResponseDataClass>
}

