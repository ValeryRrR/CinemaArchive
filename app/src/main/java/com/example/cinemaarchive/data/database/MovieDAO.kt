package com.example.cinemaarchive.data.database

import androidx.room.*
import com.example.cinemaarchive.data.entity.FilmDbEntity
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface MovieDAO {

    @Query("SELECT * FROM FilmDbEntity")
    fun getAll(): Flowable<List<FilmDbEntity>>

    @Query("SELECT * FROM FilmDbEntity WHERE id IN (:filmIds)")
    fun getAllByIds(filmIds: IntArray?): List<FilmDbEntity?>?

    @Query("SELECT EXISTS(SELECT * FROM FilmDbEntity e WHERE (:id)=e.id)")
    fun isCached(id: Int): Boolean

    @Query("SELECT * FROM FilmDbEntity WHERE id = :id")
    fun getById(id: Int): Single<FilmDbEntity?>

    @Query("SELECT * FROM FilmDbEntity LIMIT :dataBaseId, :rowsCount ")
    fun getRowsStartingAtIndex(dataBaseId: Int, rowsCount: Int): Single<List<FilmDbEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(film: FilmDbEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(films: List<FilmDbEntity>)

    @Delete
    fun delete(film: FilmDbEntity)

    @Query("DELETE FROM FilmDbEntity")
    fun deleteAll()

    @Query("SELECT count(id) = 1 FROM FilmDbEntity LIMIT 1")
    fun isEmpty(): Boolean

    @Update
    fun update(vararg films: FilmDbEntity?)

    @Query("UPDATE FilmDbEntity SET isFavorite = :isFavorite WHERE id = :id")
    fun updateIsFavoriteById(isFavorite: Boolean, id: Int)

}