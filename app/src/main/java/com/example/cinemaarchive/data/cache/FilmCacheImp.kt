package com.example.cinemaarchive.data.cache

import android.content.Context
import android.content.SharedPreferences
import com.example.cinemaarchive.data.database.MovieDatabase
import com.example.cinemaarchive.data.entity.FilmDbEntity
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class FilmCacheImp(
    private val dataBase: MovieDatabase,
    context: Context
) : FilmCache {

    private val sp: SharedPreferences =
        context.getSharedPreferences("cachePrefs", Context.MODE_PRIVATE)

    private var cachedPagesCount
        get() = getFromPref("cachedPagesCount", 0)
        set(value) = saveToPref(value, "cachedPagesCount")

    private val cacheTimeoutMinutes = 20

    private var cacheTime
        get() = getFromPref("cacheTimeout", 0)
        set(value) = saveToPref(value, "cacheTimeout")

    override fun get(filmId: Int): Single<FilmDbEntity?> {
        return dataBase.movieDao().getById(filmId)
    }

    override fun getRowsStartingAtIndex(
        startIndex: Int,
        rowsCount: Int
    ): Single<List<FilmDbEntity>> {
        return dataBase.movieDao().getRowsStartingAtIndex(startIndex, rowsCount)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getAll(): Flowable<List<FilmDbEntity>> {
        return dataBase.movieDao().getAll()
            .subscribeOn(Schedulers.io())
            .filter { it.isNotEmpty() }
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun putAll(listFilms: List<FilmDbEntity>) {
        Completable.fromAction { dataBase.movieDao().insertAll(listFilms) }
            .subscribeOn(Schedulers.io())
            .subscribe()
        cachedPagesCount++
    }

    override fun isEmpty(): Boolean {
        return cachedPagesCount == 0
    }

    override fun isExpired(): Boolean {
        return (getCurrentMinute() - cacheTime) > cacheTimeoutMinutes
    }

    override fun clearAll() {
        Completable.fromAction {
            dataBase.movieDao().deleteAll()
        }
            .subscribeOn(Schedulers.io())
            .subscribe()
        cachedPagesCount = 0
        cacheTime = getCurrentMinute()
    }

    override fun isPageCached(page: Int): Boolean{
        return page <= cachedPagesCount
    }

    private fun getCurrentMinute(): Int{
        return TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis()).toInt()
    }

    private fun saveToPref(page: Int, key: String) {
        val editor = sp.edit()
        editor.putInt(key, page)
        editor.apply()
    }

    private fun getFromPref(key: String, defValue: Int): Int {
        return sp.getInt(key, defValue)
    }
}