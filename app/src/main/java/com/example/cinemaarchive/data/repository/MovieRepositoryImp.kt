package com.example.cinemaarchive.data.repository

import android.util.Log
import com.example.cinemaarchive.data.cache.FilmCache
import com.example.cinemaarchive.data.database.Database
import com.example.cinemaarchive.data.repository.datasource.FilmDataStoreFactory
import com.example.cinemaarchive.domain.entity.Film
import com.example.cinemaarchive.domain.usecase.GetFilmCallback
import com.example.cinemaarchive.domain.repository.MovieRepository
import java.lang.IllegalStateException

class MovieRepositoryImp(
    private val filmDataStoreFactory: FilmDataStoreFactory,
    private val filmCache: FilmCache) : MovieRepository{

    override fun getFilmDetail(filmId: Int) {
        TODO("not implemented")
    }

    override fun getFilms(getFilmsCallback: GetFilmCallback, page: Int) {
        filmDataStoreFactory.createRemoteDataStore().getMovieListPage(getFilmsCallback, page)
    }

    override fun updateFavoriteList(filmId: Int, isFavorite: Boolean) {
        val film = getCachedFilmById(filmId)
            ?: throw  IllegalStateException("Film can't be null before adding to favorites")
        film.isFavorite = isFavorite
        if (isFavorite)
            Database.favoriteList.add(film)
        else if (!isFavorite) {
            Database.favoriteList.remove(film)
        }
    }

    private fun getCachedFilmById(filmId: Int): Film? {
        return filmCache.getAll().firstOrNull{ it.id == filmId }
    }
}