package com.example.cinemaarchive.di.domain


import com.example.cinemaarchive.di.data.DataModule
import com.example.cinemaarchive.domain.repository.MovieRepository
import com.example.cinemaarchive.domain.usecase.GetFavoriteListUseCase
import com.example.cinemaarchive.domain.usecase.GetFilmsUseCase
import com.example.cinemaarchive.domain.usecase.GetGenresUseCase
import com.example.cinemaarchive.domain.usecase.UpdateFavoriteListUseCase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module(includes = [DataModule::class])
class UseCasesModule {

    @Provides
    @Singleton
    fun getFilmsUseCase(movieRepository: MovieRepository): GetFilmsUseCase {
        return GetFilmsUseCase(movieRepository)
    }

    @Provides
    @Singleton
    fun updateFavoriteListUseCase(movieRepository: MovieRepository): UpdateFavoriteListUseCase {
        return UpdateFavoriteListUseCase(movieRepository)
    }

    @Provides
    @Singleton
    fun getFavoriteListUseCase(movieRepository: MovieRepository): GetFavoriteListUseCase {
        return GetFavoriteListUseCase(movieRepository)
    }

    @Provides
    @Singleton
    fun getGenresUseCase(movieRepository: MovieRepository): GetGenresUseCase {
        return GetGenresUseCase(movieRepository)
    }
}