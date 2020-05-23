package com.example.cinemaarchive.data.repository

import android.content.Context
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
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MovieRepositoryImp(
    filmDataStoreFactory: FilmDataStoreFactory,
    private val filmCache: FilmCache,
    private val context: Context,
    private val dataBase: MovieDatabase
) : MovieRepository {

    private val remoteDataSource = filmDataStoreFactory.createRemoteDataStore()
    private lateinit var getFilmsCallback: GetFilmCallback
    private val compositeDisposable = CompositeDisposable()
    private var numberOfElementsPerPage: Int = 20

    override fun getFilms(getFilmsCallback: GetFilmCallback, page: Int) {
        this.getFilmsCallback = getFilmsCallback

        if (!isThereInternetConnection(context) && filmCache.isEmpty()) {
            getFilmsCallback.onError(context.getString(R.string.no_internet))
            return
        }
        checkCache(page)
    }

    override fun updateFavoriteList(filmId: Int, isFavorite: Boolean) {
        Completable.fromCallable(FavoriteDbUpdater(filmId, isFavorite, dataBase))
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    override fun getFavoriteList(): Flowable<List<Film>> {
        return dataBase.favoriteMovieDao().getAll()
            .map { it.map { film -> film.toFilmDataEntity().toDomainFilm() } }
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getFilmDetail(filmId: Int): Single<Film?> {
        return filmCache.get(filmId).map { it.toFilmDataEntity().toDomainFilm() }
    }

    private fun checkCache(page: Int) {
        when {
            filmCache.isExpired() && isThereInternetConnection(context) -> {
                filmCache.clearAll()
                getPageFromNet(page)
            }
            filmCache.isPageCached(page) -> {
                getPageFromCache(page)
            }
            else -> {
                getPageFromNet(page)
            }
        }
    }

    private fun getPageFromCache(page: Int) {
        compositeDisposable.add(
            filmCache.getRowsStartingAtIndex(
                startIndex = (page - 1) * numberOfElementsPerPage,
                rowsCount = numberOfElementsPerPage
            )
                .map { list -> list.map { it.toFilmDataEntity().toDomainFilm() } }
                .subscribe { it ->
                    getFilmsCallback.onSuccess(it)
                })
    }

    private fun getPageFromNet(page: Int) {
        compositeDisposable.add(
            remoteDataSource.getMovieListPage(page)
                .subscribeOn(Schedulers.io())
                .map { markFavorite(it.results) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        getFilmsCallback.onSuccess(it.map {film -> film.toDomainFilm() })
                        addToCache(it.map {film -> film.toFilmDbEntity() })
                    },
                    {
                        it.printStackTrace()
                        getFilmsCallback.onError(context.getString(R.string.connection_error))
                    })
        )
    }

    private fun addToCache(films: List<FilmDbEntity>) {
        filmCache.putAll(films)
    }

    fun clearDisposable() {
        compositeDisposable.dispose()
    }

    private fun markFavorite(films: List<FilmDataEntity>): List<FilmDataEntity> {
        val favoriteIds = dataBase.favoriteMovieDao().getAllId()

        films.forEach { film ->
            if (favoriteIds.any { film.id == it }) {
                film.isFavorite = true
            }
        }
        return films
    }
}