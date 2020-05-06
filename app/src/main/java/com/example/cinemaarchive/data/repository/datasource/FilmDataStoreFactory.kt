package com.example.cinemaarchive.data.repository.datasource

import android.content.Context
import com.example.cinemaarchive.data.cache.FilmCache
import com.example.cinemaarchive.data.network.TheMovieDBApi
import com.example.cinemaarchive.data.network.TheMovieDBmApi
import java.security.AccessControlContext

class FilmDataStoreFactory(private val filmCache: FilmCache, private val context: Context) {

    fun create(): DataSource {
        return if (!filmCache.isExpired()) {
            LocalDataSource(filmCache)
        } else {
            createRemoteDataStore()
        }
    }

    fun createRemoteDataStore(): DataSource {
        val restApi: TheMovieDBApi = TheMovieDBmApi.retrofitApi
        return RemoteDataSource(restApi, filmCache, context)
    }
}