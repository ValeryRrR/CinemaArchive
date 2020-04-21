package com.example.cinemaarchive.domain

import com.example.cinemaarchive.data.entity.Film
import com.example.cinemaarchive.data.network.ThemoviedbService
import com.example.cinemaarchive.data.repository.MovieRepository

class MovieInteractor(private val themoviedbService: ThemoviedbService, private val movieRepository: MovieRepository) {

    interface GetFilmsCallback {
        fun onSuccess(repos: List<Film>)
        fun onError(error: String)
    }
}