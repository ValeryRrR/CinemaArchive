package com.example.cinemaarchive.data.repository.datasource

import android.content.Context
import com.example.cinemaarchive.data.cache.FilmCache
import com.example.cinemaarchive.data.network.TheMovieDBApi
import com.example.cinemaarchive.data.network.TheMovieDBmApi

class DataStoreFactory(private val filmCache: FilmCache, private val context: Context) {

    fun createRemoteDataStore(): RemoteDataSource {
        val restApi: TheMovieDBApi = TheMovieDBmApi.retrofitApi
        return RemoteDataSource(restApi)
    }
}