package com.example.cinemaarchive.data.repository

import com.example.cinemaarchive.data.entity.Film
import com.example.cinemaarchive.domain.repository.MovieRepository
import java.util.ArrayList

class MovieRepositoryImp: MovieRepository {
    private val cachedFilms = ArrayList<Film>()
    private val fakeFilms = ArrayList<Film>()

    val cachedOrFakeFilms: List<Film>
        get() = if (cachedFilms.size > 0)
            cachedFilms
        else
            fakeFilms

    init {
        fakeFilms.add(Film())
        fakeFilms.add(Film())
        fakeFilms.add(Film())
        fakeFilms.add(Film())
    }

    override fun addToCache(repos: List<Film>) {
        this.cachedFilms.addAll(repos)
    }

    override fun getFilmInfo(filmId: Int) {
        TODO("not implemented")
    }
}