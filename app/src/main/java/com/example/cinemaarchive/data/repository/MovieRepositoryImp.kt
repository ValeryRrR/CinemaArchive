package com.example.cinemaarchive.data.repository

import android.content.Context
import android.util.Log
import com.example.cinemaarchive.R
import com.example.cinemaarchive.data.cache.FilmCache
import com.example.cinemaarchive.data.database.MovieDatabase
import com.example.cinemaarchive.data.entity.*
import com.example.cinemaarchive.data.network.isThereInternetConnection
import com.example.cinemaarchive.data.repository.datasource.FilmDataStoreFactory
import com.example.cinemaarchive.domain.entity.Film
import com.example.cinemaarchive.domain.repository.MovieRepository
import com.example.cinemaarchive.domain.usecase.GetFilmCallback
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Callable


class MovieRepositoryImp(
    private val filmDataStoreFactory: FilmDataStoreFactory,
    private val filmCache: FilmCache,
    private val context: Context,
    private val dataBase: MovieDatabase
) : MovieRepository {

    private val remoteDataSource = filmDataStoreFactory.createRemoteDataStore()
    private lateinit var getFilmsCallback: GetFilmCallback

    override fun getFilmDetail(filmId: Int) {
        TODO("not implemented")
    }

    override fun getFilms(getFilmsCallback: GetFilmCallback, page: Int) {
        this.getFilmsCallback = getFilmsCallback

        if (!isThereInternetConnection(context)) {
            getFilmsCallback.onError(context.getString(R.string.no_internet))
            return
        }

        if (page == 1) {
            deleteAllFromDB()
            subscribeGetFromCache()
        }

        val obs = remoteDataSource.getMovieListPage(page)
        obs.subscribe { it -> addToCache(it.results.map { it.toFilmDbEntity() }) }

        //addToCache(it.results.map { it.toFilmDbEntity() })


        //response.body()!!.results.map { markFavorites(it) }
    }

    override fun updateFavoriteList(filmId: Int, isFavorite: Boolean) {
        Completable.fromCallable(FavoriteListUpdater(filmId, isFavorite, dataBase))
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    override fun getFavoriteList(): Flowable<List<Film>> {
        return dataBase.favoriteMovieDao().getAll()
            .map { it.map { film -> film.toFilmDataEntity().toDomainFilm() } }
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun subscribeGetFromCache() {
        val disposable = dataBase.movieDao().getAll()
            .subscribeOn(Schedulers.io())
            .filter { it.isNotEmpty() }
            .map { it.map { film -> film.toFilmDataEntity().toDomainFilm() } }
            .observeOn(AndroidSchedulers.mainThread())

        //todo fix
        disposable.subscribe { getFilmsCallback.onSuccess(it) }
    }

    //TODO clearing db for testing, remove it after
    private fun deleteAllFromDB() {
        Completable.fromAction {
            dataBase.movieDao().deleteAll()
            dataBase.favoriteMovieDao().deleteAll()
            Log.i(
                "Flowable",
                "Completable.fromAction запрошена 1 стр. очистил все данные в бд"
            )
        }
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    private fun addToCache(films: List<FilmDbEntity>) {
        Completable.fromAction { dataBase.movieDao().insertAll(films) }
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    internal class FavoriteListUpdater(
        private val filmId: Int,
        private val isFavorite: Boolean,
        private val dataBase: MovieDatabase
    ) : Callable<Unit> {

        override fun call() {
            val film = getCachedFilmById(filmId)
                .toFilmDataEntity()
                .toFilmFavoriteEntity()
            film.isFavorite = isFavorite
            if (isFavorite) {
                dataBase.favoriteMovieDao().insert(film)
            } else if (!isFavorite) {
                dataBase.favoriteMovieDao().deleteByFilmId(film.id)
            }
        }

        private fun getCachedFilmById(filmId: Int): FilmDbEntity {
            return dataBase.movieDao().getById(filmId)
                ?: throw IllegalStateException("Film can't be null before updating favorites")
        }
    }

    /*  TODO fix
    private fun markFavorites(film: FavoriteMovieEntity) {
        Log.i("markFavorites", film.name + film.isFavorite.toString())
        if (film in favorites){
            film.isFavorite = true
        }
    }*/
}