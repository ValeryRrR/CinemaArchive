package com.example.cinemaarchive.data.cache

import com.example.cinemaarchive.data.entity.FilmDataEntity

interface FilmCache {
    fun get(filmId: Int): FilmDataEntity?
    fun getAll(): List<FilmDataEntity>
    fun put(film: FilmDataEntity)
    fun putAll(listFilm: List<FilmDataEntity>)
    fun isCached(filmId: Int): Boolean
    fun isExpired(): Boolean
    fun clearAll()
}