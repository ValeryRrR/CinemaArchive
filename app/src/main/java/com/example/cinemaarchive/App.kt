package com.example.cinemaarchive

import android.app.Application
import com.example.cinemaarchive.data.network.BASE_URL
import com.example.cinemaarchive.data.network.ThemoviedbService
import com.example.cinemaarchive.data.repository.MovieRepository
import com.example.cinemaarchive.domain.MovieInteractor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class App: Application() {
    lateinit var themoviedbService: ThemoviedbService
    lateinit var movieInteractor: MovieInteractor
    var movieRepository = MovieRepository()

    override fun onCreate() {
        super.onCreate()


    }

    private fun initInteractor(){
        movieInteractor = MovieInteractor(themoviedbService, movieRepository)
    }

    private fun initRetrofit() {
        themoviedbService = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(ThemoviedbService::class.java)
        }

    companion object {
        var instance: App? = null
            private set
    }
}