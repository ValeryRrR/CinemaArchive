package com.example.cinemaarchive.data.database

import androidx.room.*
import com.example.cinemaarchive.data.entity.FilmDbEntity

@Dao
interface MovieDAO {

    @Query("SELECT * FROM FilmDbEntity")
    fun getAll(): List<FilmDbEntity>

    @Query("SELECT * FROM FilmDbEntity WHERE id IN (:filmIds)")
    fun getAllByIds(filmIds: IntArray?): List<FilmDbEntity?>?

    @Query("SELECT EXISTS(SELECT * FROM FilmDbEntity e WHERE (:id)=e.id)")
    fun isCached(id: Int): Boolean

    @Query("SELECT * FROM FilmDbEntity WHERE id LIKE (:id)")
    fun getById(id: Int): FilmDbEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(film: FilmDbEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(films: List<FilmDbEntity>)

    @Delete
    fun delete(film: FilmDbEntity)

    @Update
    fun update(vararg films: FilmDbEntity?)

}