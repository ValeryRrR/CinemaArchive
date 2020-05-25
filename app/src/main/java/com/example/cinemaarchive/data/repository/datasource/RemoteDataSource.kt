package com.example.cinemaarchive.data.repository.datasource

import com.example.cinemaarchive.data.entity.*
import com.example.cinemaarchive.data.network.ResponseDataClass
import com.example.cinemaarchive.data.network.TheMovieDBApi
import io.reactivex.Single


class RemoteDataSource(
    private val theMovieDBApi: TheMovieDBApi
) {

    fun getMovieListPage(page: Int): Single<ResponseDataClass> {
        return theMovieDBApi.getListFilms(
            page
        ) //todo move to interceptor API_KEY, RU_LANG
    }

    fun getFilmEntityDetails(filmId: FilmDataEntity): FilmDataEntity? {
        TODO("not implemented")
    }
}