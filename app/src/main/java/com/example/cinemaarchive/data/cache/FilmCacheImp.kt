package com.example.cinemaarchive.data.cache

import com.example.cinemaarchive.data.database.MovieDatabase
import com.example.cinemaarchive.data.entity.FilmDbEntity
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class FilmCacheImp(private val dataBase: MovieDatabase): FilmCache {

    override fun get(filmId: Int): FilmDbEntity? {
        return null  // todo not implemented cachedFilms.firstOrNull { filmId == it.id }
    }

    override fun getRowsStartingAtIndex(dataBaseId: Int, rowsCount: Int): Single<List<FilmDbEntity>> {
        return dataBase.movieDao().getRowsStartingAtIndex(dataBaseId, rowsCount)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getAll(): Flowable<List<FilmDbEntity>> {
        return dataBase.movieDao().getAll()
            .subscribeOn(Schedulers.io())
            .filter {
                it.isNotEmpty() }
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun putAll(listFilms: List<FilmDbEntity>) {
        Completable.fromAction { dataBase.movieDao().insertAll(listFilms) }
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    override fun isEmpty(): Boolean {
        return false //TODO("not implemented")
    }

    override fun isExpired(): Boolean {
        return false // todo not implemented
    }

    override fun clearAll() {
        Completable.fromAction {
            dataBase.movieDao().deleteAll()
        }
            .subscribeOn(Schedulers.io())
            .subscribe()
    }
}