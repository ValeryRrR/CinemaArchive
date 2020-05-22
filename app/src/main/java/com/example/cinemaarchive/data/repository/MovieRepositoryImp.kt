package com.example.cinemaarchive.data.repository

import android.content.Context
import android.content.SharedPreferences
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
import io.reactivex.disposables.CompositeDisposable
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

    private val sp: SharedPreferences =
        context.getSharedPreferences("page_prefs", Context.MODE_PRIVATE)
    private var cachedPagesCount
        get() = getPageFromSharedPreference()
        set(value) = savePageToSharedPreference(value)

    private val compositeDisposable = CompositeDisposable()

    override fun getFilmDetail(filmId: Int) {
        TODO("not implemented")
    }

    override fun getFilms(getFilmsCallback: GetFilmCallback, page: Int) {
        this.getFilmsCallback = getFilmsCallback

        if (!isThereInternetConnection(context) && cachedPagesCount == 0) {
            getFilmsCallback.onError(context.getString(R.string.no_internet))
            return
        }

        if (cachedPagesCount > 0) {
            when {
                filmCache.isExpired() -> {
                    getPageFromNet(page)
                }
                page <= cachedPagesCount -> {
                    getPageFromCache(page)
                }
                else -> {
                    getPageFromNet(page)
                }
            }
            return
        }

        if (page == 1) {
            getFirstPage()
        }
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

    private fun getPageFromCache(page: Int) {
        compositeDisposable.add(
            filmCache.getRowsStartingAtIndex((page - 1) * 20, 20)
                .map { list -> list.map { it.toFilmDataEntity().toDomainFilm() } }
                .subscribe { it ->
                    getFilmsCallback.onSuccess(it)
                })
    }

    private fun getFirstPage() {
        filmCache.clearAll()
        cachedPagesCount = 0
        getPageFromNet(1)
    }

    private fun getPageFromNet(page: Int) {
        compositeDisposable.add(
            remoteDataSource.getMovieListPage(page)
                .subscribe(
                    {
                        getFilmsCallback.onSuccess(it.results.map { it.toDomainFilm() })
                        addToCache(it.results.map { it.toFilmDbEntity() })
                    },
                    {
                        getFilmsCallback.onError(context.getString(R.string.connection_error))
                    })
        )
    }

    private fun addToCache(films: List<FilmDbEntity>) {
        filmCache.putAll(films)
        cachedPagesCount++
    }


    fun clearDisposable() {
        compositeDisposable.dispose()
    }

    private fun savePageToSharedPreference(page: Int) {
        val editor = sp.edit()
        editor.putInt("int_key", page)
        editor.apply()
    }

    private fun getPageFromSharedPreference(): Int {
        return sp.getInt("int_key", 0)
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