package com.example.cinemaarchive.domain.repository

import com.example.cinemaarchive.domain.entity.Film
import com.example.cinemaarchive.domain.usecase.GetFilmCallback
import io.reactivex.Flowable

interface MovieRepository {
    fun getFilmDetail(filmId: Int)
    fun updateFavoriteList(filmId: Int, isFavorite: Boolean)
    fun getFilms(getFilmsCallback: GetFilmCallback, page: Int)
    fun getFavoriteList(): Flowable<List<Film>>
}