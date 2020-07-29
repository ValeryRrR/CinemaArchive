package com.example.cinemaarchive.di.data

import com.example.cinemaarchive.data.cache.FilmCache
import com.example.cinemaarchive.data.database.MovieDatabase
import com.example.cinemaarchive.data.network.TheMovieDBApi
import com.example.cinemaarchive.data.repository.datasource.DataStoreFactory
import com.example.cinemaarchive.domain.repository.MovieRepository
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DataModule::class])
interface DataComponent {
    fun provideDataBase(): MovieDatabase
    fun provideCaches(): FilmCache
    fun provideRetrofit(): TheMovieDBApi
    fun provideDataStoreFactory(): DataStoreFactory
    fun provideRepository(): MovieRepository
}