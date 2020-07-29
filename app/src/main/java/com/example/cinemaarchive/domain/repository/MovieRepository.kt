package com.example.cinemaarchive.domain.repository

import com.example.cinemaarchive.data.entity.Genres
import com.example.cinemaarchive.domain.entity.Film
import com.example.cinemaarchive.domain.usecase.GetFilmCallback
import io.reactivex.Flowable
import io.reactivex.Single

interface MovieRepository {
    fun getFilmDetail(filmId: Int): Single<Film?>
    fun updateFavoriteList(filmId: Int, isFavorite: Boolean)
    fun getFilms(getFilmsCallback: GetFilmCallback, page: Int)
    fun getFavoriteList(): Flowable<List<Film>>
    fun getAllGenres(): Single<Genres>
}