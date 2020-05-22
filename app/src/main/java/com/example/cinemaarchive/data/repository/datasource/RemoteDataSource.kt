package com.example.cinemaarchive.data.repository.datasource

import com.example.cinemaarchive.data.entity.*
import com.example.cinemaarchive.data.network.API_KEY
import com.example.cinemaarchive.data.network.RU_LANG
import com.example.cinemaarchive.data.network.ResponseDataClass
import com.example.cinemaarchive.data.network.TheMovieDBApi
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class RemoteDataSource(
    private val theMovieDBApi: TheMovieDBApi
) {

    fun getMovieListPage(page: Int): Single<ResponseDataClass> {
        return theMovieDBApi.getListFilms(
            API_KEY,
            RU_LANG,
            page
        ) //todo move to interceptor API_KEY, RU_LANG
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getFilmEntityDetails(filmId: FilmDataEntity): FilmDataEntity? {
        TODO("not implemented")
    }
}