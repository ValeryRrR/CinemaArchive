package com.example.cinemaarchive.di.presentation


import com.example.cinemaarchive.di.domain.UseCasesModule
import com.example.cinemaarchive.presentation.view.FavoriteListFragment
import com.example.cinemaarchive.presentation.view.MainListFragment
import com.example.cinemaarchive.presentation.view.detail.DetailFragment
import com.example.cinemaarchive.presentation.view.detail.DetailViewModelFactory
import com.example.cinemaarchive.presentation.viewModel.FavoriteViewModelFactory
import com.example.cinemaarchive.presentation.viewModel.MainViewModelFactory
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ViewModelFactoryModule::class, UseCasesModule::class])
interface ViewModelComponent {
    fun inject(fragment: MainListFragment)
    fun inject(fragment: FavoriteListFragment)
    fun inject(fragment: DetailFragment)
    fun provideFavoriteViewModelFactory(): FavoriteViewModelFactory
    fun provideMainViewModelFactory(): MainViewModelFactory
    fun provideDetailViewModelFactory(): DetailViewModelFactory
}