package com.example.cinemaarchive.data.repository

import android.util.Log
import com.example.cinemaarchive.App
import com.example.cinemaarchive.data.cache.FilmCache
import com.example.cinemaarchive.data.database.Database
import com.example.cinemaarchive.data.entity.*
import com.example.cinemaarchive.data.repository.datasource.FilmDataStoreFactory
import com.example.cinemaarchive.domain.entity.Film
import com.example.cinemaarchive.domain.usecase.GetFilmCallback
import com.example.cinemaarchive.domain.repository.MovieRepository
import java.lang.IllegalStateException

class MovieRepositoryImp(
    private val filmDataStoreFactory: FilmDataStoreFactory,
    private val filmCache: FilmCache
) : MovieRepository {

    private val dataBase = App.instance!!.database

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
        if (isFavorite) {
            //Database.favoriteList.add(film)
            //dataBase.favoriteMovieDao().deleteAll()
            Log.i("FILM", film.name + "Added to Favorite Table BD")
            dataBase.favoriteMovieDao().insert(film.toFilmFavoriteEntity())
        }
        else if (!isFavorite) {
            //Database.favoriteList.remove(film)
            dataBase.favoriteMovieDao().delete(film.toFilmFavoriteEntity())
        }
    }

    override fun updateFavoriteListByPosition(filmId: Int, isFavorite: Boolean, position: Int) {
        val film = getCachedFilmById(filmId)
            ?: throw IllegalStateException("Film can't be null before adding to favorites")
        film.isFavorite = isFavorite
        if (isFavorite)
            //Database.favoriteList.add(position, film)
        else if (!isFavorite) {
            //Database.favoriteList.remove(film)
            dataBase.favoriteMovieDao().delete(film.toFilmFavoriteEntity())
        }
    }

    private fun getCachedFilmById(filmId: Int): FilmDataEntity? {
        return dataBase.movieDao().getAll().asSequence()
            .firstOrNull { it.id == filmId }?.toFilmDataEntity()
    }

    override fun isFavoriteListEmpty(): Boolean {
        //return Database.favoriteList.isEmpty()
        return false
    }

    override fun getFavoriteList(): List<Film> {
        //return Database.favoriteList.map { it.toDomainFilm() }
        return dataBase.favoriteMovieDao().getAll().map {
            it.toDomainFilm() }
    }
}