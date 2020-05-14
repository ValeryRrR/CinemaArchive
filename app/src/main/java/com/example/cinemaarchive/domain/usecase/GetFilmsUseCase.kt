package com.example.cinemaarchive.domain.usecase

import com.example.cinemaarchive.data.network.*
import com.example.cinemaarchive.domain.repository.MovieRepository

class GetFilmsUseCase(
    private var movieRepository: MovieRepository
) {

    fun getFilms(getFilmsCallback: GetFilmCallback, page: Int){
        movieRepository.getFilms(getFilmsCallback, page)
    }
}