package com.example.cinemaarchive.data.repository

import com.example.cinemaarchive.data.entity.Film
import java.util.ArrayList

class MovieRepository {
    private val cachedRepos = ArrayList<Film>()
    private val fakeRepos = ArrayList<Film>()

    val cachedOrFakeRepos: List<Film>
        get() = if (cachedRepos.size > 0)
            cachedRepos
        else
            fakeRepos

    init {
        fakeRepos.add(Film())
        fakeRepos.add(Film())
        fakeRepos.add(Film())
        fakeRepos.add(Film())
    }

    fun addToCache(repos: List<Film>) {
        this.cachedRepos.addAll(repos)
    }
}