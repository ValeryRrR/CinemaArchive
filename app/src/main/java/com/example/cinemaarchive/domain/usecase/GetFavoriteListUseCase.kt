package com.example.cinemaarchive.domain.usecase

import com.example.cinemaarchive.domain.entity.Film
import com.example.cinemaarchive.domain.repository.MovieRepository

class GetFavoriteListUseCase(
    private var movieRepository: MovieRepository
) {

    fun getFavoriteList(): List<Film>{
        return movieRepository.getFavoriteList()
    }

    fun isFavoriteListEmpty(): Boolean{
        return movieRepository.isFavoriteListEmpty()
    }
}