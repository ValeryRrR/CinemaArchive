package com.example.cinemaarchive.data.cache

import com.example.cinemaarchive.data.entity.FilmDbEntity
import io.reactivex.Flowable
import io.reactivex.Single

interface FilmCache {
    fun get(filmId: Int): FilmDbEntity?
    fun getAll(): Flowable<List<FilmDbEntity>>
    fun getRowsStartingAtIndex(dataBaseId: Int, rowsCount: Int): Single<List<FilmDbEntity>>
    fun putAll(listFilms: List<FilmDbEntity>)
    fun isEmpty(): Boolean
    fun isExpired(): Boolean
    fun clearAll()
}