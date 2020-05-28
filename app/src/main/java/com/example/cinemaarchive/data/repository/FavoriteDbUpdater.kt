package com.example.cinemaarchive.data.repository

import com.example.cinemaarchive.data.database.MovieDatabase
import com.example.cinemaarchive.data.entity.FilmDbEntity
import com.example.cinemaarchive.data.entity.toFilmDataEntity
import com.example.cinemaarchive.data.entity.toFilmFavoriteEntity
import java.util.concurrent.Callable

internal class FavoriteDbUpdater (
    private val filmId: Int,
    private val isFavorite: Boolean,
    private val dataBase: MovieDatabase
) : Callable<Unit> {

    override fun call() {
        dataBase.movieDao().updateIsFavoriteById(isFavorite, filmId)
        val film = getCachedFilmById(filmId)
            .toFilmDataEntity()
            .toFilmFavoriteEntity()
        film.isFavorite = isFavorite
        if (isFavorite) {
            dataBase.favoriteMovieDao().insert(film)
        } else if (!isFavorite) {
            dataBase.favoriteMovieDao().deleteByFilmId(film.id)
        }
    }

    private fun getCachedFilmById(filmId: Int): FilmDbEntity {
        return dataBase.movieDao().getById(filmId).blockingGet()
            ?: throw IllegalStateException("Film can't be null before updating favorites")
    }
}