package com.example.cinemaarchive

import android.app.Application
import com.example.cinemaarchive.data.network.TheMovieDBmApi
import com.example.cinemaarchive.data.network.TheMovieDBService
import com.example.cinemaarchive.data.repository.MovieRepositoryImp
import com.example.cinemaarchive.domain.usecase.GetFilmsUseCase

class App: Application() {
    lateinit var theMovieDBService: TheMovieDBService
    lateinit var getFilmsUseCase: GetFilmsUseCase
    lateinit var movieRepository: MovieRepositoryImp

    override fun onCreate() {
        super.onCreate()
        instance = this
        initRepository()
        initRetrofit()
        initUseCases()
    }

    private fun initRepository() {
        movieRepository= MovieRepositoryImp()
    }

    private fun initUseCases(){
        getFilmsUseCase = GetFilmsUseCase(theMovieDBService, movieRepository)
    }

    private fun initRetrofit() {
        theMovieDBService = TheMovieDBmApi.retrofitService
    }

    companion object {
        var instance: App? = null
            private set
    }
}