package com.example.cinemaarchive.di.presentation


import com.example.cinemaarchive.di.domain.UseCasesModule
import com.example.cinemaarchive.domain.usecase.GetFavoriteListUseCase
import com.example.cinemaarchive.domain.usecase.GetFilmsUseCase
import com.example.cinemaarchive.domain.usecase.GetGenresUseCase
import com.example.cinemaarchive.domain.usecase.UpdateFavoriteListUseCase
import com.example.cinemaarchive.presentation.view.detail.DetailViewModelFactory
import com.example.cinemaarchive.presentation.viewModel.*
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [UseCasesModule::class])
class ViewModelFactoryModule() {

    @Singleton
    @Provides
    fun provideFavoriteViewModelFactory(
        updateFavoriteListUseCase: UpdateFavoriteListUseCase,
        getFavoriteListUseCase: GetFavoriteListUseCase
    ): FavoriteViewModelFactory {
        return FavoriteViewModelFactory(updateFavoriteListUseCase, getFavoriteListUseCase)
    }

    @Singleton
    @Provides
    fun provideMainViewModelFactory(
        updateFavoriteListUseCase: UpdateFavoriteListUseCase,
        getFilmsUseCase: GetFilmsUseCase
    ): MainViewModelFactory {
        return MainViewModelFactory(updateFavoriteListUseCase, getFilmsUseCase)
    }

    @Singleton
    @Provides
    fun provideDetailViewModelFactory(
        getGenresUseCase: GetGenresUseCase
    ): DetailViewModelFactory {
        return DetailViewModelFactory(
            getGenresUseCase
        )
    }
}