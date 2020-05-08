package com.example.cinemaarchive.domain.repository

import com.example.cinemaarchive.domain.entity.Film
import com.example.cinemaarchive.domain.usecase.GetFilmCallback

interface MovieRepository {
    fun getFilmDetail(filmId: Int)
    fun updateFavoriteList(filmId: Int, isFavorite: Boolean)
    fun updateFavoriteListByPosition(filmId: Int, isFavorite: Boolean, position: Int)
    fun getFilms(getFilmsCallback: GetFilmCallback, page: Int)
    fun getFavoriteList(): List<Film>
    fun isFavoriteListEmpty(): Boolean
}