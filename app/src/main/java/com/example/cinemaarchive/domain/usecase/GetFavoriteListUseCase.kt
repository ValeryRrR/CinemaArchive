package com.example.cinemaarchive.domain.usecase

import com.example.cinemaarchive.domain.entity.Film
import com.example.cinemaarchive.domain.repository.MovieRepository
import io.reactivex.Flowable

class GetFavoriteListUseCase(
    private var movieRepository: MovieRepository
) {

    fun getFavoriteList(): Flowable<List<Film>> {
        return movieRepository.getFavoriteList()
    }
}