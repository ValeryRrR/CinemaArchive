package com.example.cinemaarchive.di.data

import android.content.Context
import com.example.cinemaarchive.data.cache.FilmCache
import com.example.cinemaarchive.data.cache.FilmCacheImp
import com.example.cinemaarchive.data.database.MovieDatabase
import com.example.cinemaarchive.data.network.TheMovieDBApi
import com.example.cinemaarchive.data.network.TheMovieDBmApi
import com.example.cinemaarchive.data.repository.MovieRepositoryImp
import com.example.cinemaarchive.data.repository.datasource.DataStoreFactory
import com.example.cinemaarchive.di.AppModule
import com.example.cinemaarchive.domain.repository.MovieRepository
import java.lang.IllegalStateException
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [AppModule::class])
class DataModule {

    @Provides
    @Singleton
    fun provideDataBase(context: Context): MovieDatabase {
        return MovieDatabase.getInstance(context)
            ?: throw IllegalStateException("Unable to create MovieDatabase")
    }

    @Provides
    @Singleton
    fun provideCaches(context: Context): FilmCache {
        return FilmCacheImp(provideDataBase(context), context)
    }

    @Provides
    @Singleton
    fun provideRetrofit(): TheMovieDBApi {
        return TheMovieDBmApi.retrofitApi
    }

    @Provides
    @Singleton
    fun provideDataStoreFactory(filmCache: FilmCache, context: Context): DataStoreFactory {
        return DataStoreFactory(filmCache, context)
    }

    @Provides
    @Singleton
    fun provideRepository(
        dataStoreFactory: DataStoreFactory,
        filmCache: FilmCache,
        context: Context,
        database: MovieDatabase
    ): MovieRepository {
        return MovieRepositoryImp(dataStoreFactory, filmCache, context, database)
    }
}