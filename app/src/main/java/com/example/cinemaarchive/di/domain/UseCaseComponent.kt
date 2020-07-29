package com.example.cinemaarchive.di.domain

import com.example.cinemaarchive.domain.usecase.GetFavoriteListUseCase
import com.example.cinemaarchive.domain.usecase.GetFilmsUseCase
import com.example.cinemaarchive.domain.usecase.GetGenresUseCase
import com.example.cinemaarchive.domain.usecase.UpdateFavoriteListUseCase
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [UseCasesModule::class])
interface UseCaseComponent {
    fun getFilmsUseCase(): GetFilmsUseCase
    fun updateFavoriteListUseCase(): UpdateFavoriteListUseCase
    fun getFavoriteListUseCase(): GetFavoriteListUseCase
    fun getGenresUseCase(): GetGenresUseCase
}