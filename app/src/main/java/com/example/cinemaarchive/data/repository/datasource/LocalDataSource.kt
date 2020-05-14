package com.example.cinemaarchive.data.repository.datasource

import com.example.cinemaarchive.data.cache.FilmCache
import com.example.cinemaarchive.data.entity.FilmDataEntity
import com.example.cinemaarchive.data.entity.toDomainFilm
import com.example.cinemaarchive.domain.usecase.GetFilmCallback

class LocalDataSource(private val filmCache: FilmCache): DataSource {
    override fun getMovieListPage(getFilmsCallback: GetFilmCallback, page: Int){
        if (filmCache.getAll().isNotEmpty()) {
            getFilmsCallback.onSuccess(filmCache.getAll().map { it.toDomainFilm() })
        }else
            getFilmsCallback.onError("Cache is empty")
    }

    override fun getFilmEntityDetails(filmId: FilmDataEntity): FilmDataEntity? {
        TODO("not implemented")
    }
}