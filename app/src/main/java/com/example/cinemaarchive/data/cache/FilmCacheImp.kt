package com.example.cinemaarchive.data.cache

import com.example.cinemaarchive.data.entity.FilmDataEntity
import java.util.ArrayList

class FilmCacheImp : FilmCache {
    private val cachedFilms = ArrayList<FilmDataEntity>()

    override fun get(filmId: Int): FilmDataEntity? {
        return cachedFilms.firstOrNull { filmId == it.id }
    }

    override fun getAll(): List<FilmDataEntity> {
        return cachedFilms
    }

    override fun put(film: FilmDataEntity) {
        cachedFilms.add(film)
    }

    override fun putAll(listFilm: List<FilmDataEntity>) {
        cachedFilms.addAll(listFilm)
    }

    override fun isCached(filmId: Int): Boolean {
        return cachedFilms.any { filmId == it.id }

    }

    override fun isExpired(): Boolean {
        return cachedFilms.isEmpty()
    }

    override fun clearAll() {
        cachedFilms.clear()
    }
}