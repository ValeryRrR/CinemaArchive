package com.example.cinemaarchive.data.cache

import com.example.cinemaarchive.domain.entity.Film
import java.util.ArrayList

class FilmCacheImp : FilmCache {
    private val cachedFilms = ArrayList<Film>()

    override fun get(filmId: Int): Film? {
        return if (isCached(filmId))
            cachedFilms.filter { filmId == it.id }[0]
        else null
    }

    override fun getAll(): List<Film> {
        return cachedFilms
    }

    override fun put(film: Film) {
        cachedFilms.add(film)
    }

    override fun putAll(listFilm: List<Film>) {
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