package com.example.cinemaarchive

import android.app.Application
import com.example.cinemaarchive.data.cache.FilmCache
import com.example.cinemaarchive.data.cache.FilmCacheImp
import com.example.cinemaarchive.data.network.TheMovieDBmApi
import com.example.cinemaarchive.data.network.TheMovieDBApi
import com.example.cinemaarchive.data.repository.MovieRepositoryImp
import com.example.cinemaarchive.data.repository.datasource.FilmDataStoreFactory
import com.example.cinemaarchive.domain.usecase.GetFilmsUseCase
import com.example.cinemaarchive.domain.usecase.UpdateFavoriteListUseCase

class App: Application() {
    lateinit var theMovieDBApi: TheMovieDBApi
    lateinit var getFilmsUseCase: GetFilmsUseCase
    lateinit var movieRepository: MovieRepositoryImp
    lateinit var filmDataStoreFactory: FilmDataStoreFactory
    lateinit var updateFavoriteListUseCase: UpdateFavoriteListUseCase
    lateinit var filmCache: FilmCache


    override fun onCreate() {
        super.onCreate()
        instance = this
        initCaches()
        initFactories()
        initRepository()
        initRetrofit()
        initUseCases()
    }

    private fun initCaches() {
        filmCache = FilmCacheImp()
    }


    private fun initFactories() {
        filmDataStoreFactory = FilmDataStoreFactory(filmCache, applicationContext)
    }

    private fun initRepository() {
        movieRepository = MovieRepositoryImp(filmDataStoreFactory, filmCache)
    }

    private fun initUseCases(){
        getFilmsUseCase = GetFilmsUseCase(theMovieDBApi, movieRepository)
        updateFavoriteListUseCase = UpdateFavoriteListUseCase(movieRepository)
    }

    private fun initRetrofit() {
        theMovieDBApi = TheMovieDBmApi.retrofitApi
    }

    companion object {
        var instance: App? = null
            private set
    }
}