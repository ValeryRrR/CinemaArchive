package com.example.cinemaarchive.data.cache

import com.example.cinemaarchive.domain.entity.Film

interface FilmCache {
    fun get(filmId: Int): Film?
    fun getAll(): List<Film>
    fun put(film: Film)
    fun putAll(listFilm: List<Film>)
    fun isCached(filmId: Int): Boolean
    fun isExpired(): Boolean
    fun clearAll()
}