package com.example.cinemaarchive.domain.repository

import com.example.cinemaarchive.data.entity.Film

interface MovieRepository {
    fun addToCache(repos: List<Film>)
    fun getFilmInfo(filmId: Int)
}