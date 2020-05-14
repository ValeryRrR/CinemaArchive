package com.example.cinemaarchive.data.repository.datasource

import android.content.Context
import com.example.cinemaarchive.App
import com.example.cinemaarchive.data.cache.FilmCache
import com.example.cinemaarchive.data.entity.*
import com.example.cinemaarchive.data.network.API_KEY
import com.example.cinemaarchive.data.network.RU_LANG
import com.example.cinemaarchive.data.network.ResponseDataClass
import com.example.cinemaarchive.data.network.TheMovieDBApi
import com.example.cinemaarchive.data.network.isThereInternetConnection
import com.example.cinemaarchive.domain.usecase.GetFilmCallback
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
        if (!isThereInternetConnection(context)){
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
                            filmCache.clearAll()
                        }
                        //response.body()!!.results.map { markFavorites(it) }
                        filmCache.putAll(response.body()!!.results)
                        dataBase.movieDao().insertAll(response.body()!!.results.map { it.toFilmDbEntity() })



                        getFilmsCallback.onSuccess(dataBase.movieDao().getAll().map { it.toFilmDataEntity().toDomainFilm() })
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

 /*  TODO fix
    private fun markFavorites(film: FavoriteMovieEntity) {
        Log.i("markFavorites", film.name + film.isFavorite.toString())
        if (film in favorites){
            film.isFavorite = true
        }
    }*/
}