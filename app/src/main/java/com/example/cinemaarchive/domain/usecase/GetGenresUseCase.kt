package com.example.cinemaarchive.domain.usecase

import com.example.cinemaarchive.data.entity.Genres
import com.example.cinemaarchive.domain.repository.MovieRepository
import io.reactivex.Single

class GetGenresUseCase(
    private var movieRepository: MovieRepository
) {

    fun getGenres(): Single<Genres> {
        return movieRepository.getAllGenres()
    }
}