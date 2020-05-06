package com.example.cinemaarchive.data.repository.datasource

import com.example.cinemaarchive.domain.entity.Film
import com.example.cinemaarchive.domain.usecase.GetFilmCallback

interface DataSource {
    fun getMovieListPage(getFilmsCallback: GetFilmCallback, page: Int)
    fun getFilmEntityDetails(filmId: Film): Film?
}