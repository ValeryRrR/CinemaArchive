package com.example.cinemaarchive.data.network

import com.example.cinemaarchive.data.entity.Genres
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface TheMovieDBApi {
    @GET("discover/movie")
    fun getListFilms(
        @Query("page") pageIndex: Int
    ): Single<ResponseDataClass>

    @GET("genre/movie/list")
    fun getAllGenres(): Single<Genres>
}

