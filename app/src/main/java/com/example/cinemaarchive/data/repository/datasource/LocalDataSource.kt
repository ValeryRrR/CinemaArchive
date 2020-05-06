package com.example.cinemaarchive.data.repository.datasource

import com.example.cinemaarchive.data.cache.FilmCache
import com.example.cinemaarchive.domain.entity.Film
import com.example.cinemaarchive.domain.usecase.GetFilmCallback

class LocalDataSource(private val filmCache: FilmCache): DataSource {
    override fun getMovieListPage(getFilmsCallback: GetFilmCallback, page: Int){
        if (filmCache.getAll().isNotEmpty()) {
            getFilmsCallback.onSuccess(filmCache.getAll())
        }else
            getFilmsCallback.onError("Cache is empty")
    }

    override fun getFilmEntityDetails(filmId: Film): Film? {
        TODO("not implemented")
    }
}