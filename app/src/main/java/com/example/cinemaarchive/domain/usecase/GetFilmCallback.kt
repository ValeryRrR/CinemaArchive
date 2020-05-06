package com.example.cinemaarchive.domain.usecase

import com.example.cinemaarchive.domain.entity.Film

interface GetFilmCallback {
    fun onSuccess(films: List<Film>)
    fun onError(error: String)
}