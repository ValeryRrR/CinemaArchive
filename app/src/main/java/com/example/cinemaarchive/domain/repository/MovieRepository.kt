package com.example.cinemaarchive.domain.repository

import com.example.cinemaarchive.domain.usecase.GetFilmCallback
import com.example.cinemaarchive.domain.entity.Film

interface MovieRepository {
    fun getFilmDetail(filmId: Int)
    fun updateFavoriteList(filmId: Int, isFavorite: Boolean)
    fun getFilms(getFilmsCallback: GetFilmCallback, page: Int)
    fun getFavoriteList(): List<Film>
    fun isFavoriteListEmpty(): Boolean
}