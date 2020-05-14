package com.example.cinemaarchive.domain.usecase

import com.example.cinemaarchive.domain.repository.MovieRepository

class UpdateFavoriteListUseCase(
    private var movieRepository: MovieRepository
){
    fun updateFavoriteList(filmId: Int, isFavorite: Boolean){
        movieRepository.updateFavoriteList(filmId, isFavorite)
    }

    fun updateFavoriteListByPosition(filmId: Int, isFavorite: Boolean, position: Int){
        movieRepository.updateFavoriteListByPosition(filmId, isFavorite, position)
    }
}