package com.example.cinemaarchive.data.repository.datasource

import android.content.Context
import android.util.Log
import com.example.cinemaarchive.App
import com.example.cinemaarchive.data.cache.FilmCache
import com.example.cinemaarchive.data.entity.*
import com.example.cinemaarchive.data.network.API_KEY
import com.example.cinemaarchive.data.network.RU_LANG
import com.example.cinemaarchive.data.network.ResponseDataClass
import com.example.cinemaarchive.data.network.TheMovieDBApi
import com.example.cinemaarchive.data.network.isThereInternetConnection
import com.example.cinemaarchive.domain.usecase.GetFilmCallback

import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RemoteDataSource(
    private val theMovieDBApi: TheMovieDBApi,
    private val filmCache: FilmCache,
    private val context: Context
) : DataSource {

    val dataBase = App.instance!!.database

    override fun getMovieListPage(getFilmsCallback: GetFilmCallback, page: Int) {
        if (!isThereInternetConnection(context)) {
            getFilmsCallback.onError("Check your internet connection")
            return
        }
        theMovieDBApi.getListFilms(API_KEY, RU_LANG, page)
            .enqueue(object : Callback<ResponseDataClass> {
                override fun onResponse(
                    call: Call<ResponseDataClass>,
                    response: Response<ResponseDataClass>
                ) {
                    if (response.isSuccessful) {
                        if (page == 1) {
                            Completable.fromAction {
                                dataBase.movieDao().deleteAll()
                                dataBase.favoriteMovieDao().deleteAll()
                                Log.i("Flowable", "Completable.fromAction запрошена 1 стр. очистил все данные в бд = $page")
                                //TODO clearing db for testing, remove it after
                            }
                                .subscribeOn(Schedulers.io())
                                .subscribe()
                        }
                        filmCache.putAll(response.body()!!.results)

                        Completable.fromAction {
                            dataBase.movieDao()
                                .insertAll(response.body()!!.results.map { it.toFilmDbEntity() })
                        }
                            .subscribeOn(Schedulers.io())
                            .subscribe()

                    }
                }

                override fun onFailure(call: Call<ResponseDataClass>, t: Throwable) {
                    getFilmsCallback.onError(t.message.toString())
                }
            })
    }

    override fun getFilmEntityDetails(filmId: FilmDataEntity): FilmDataEntity? {
        TODO("not implemented")
    }
}