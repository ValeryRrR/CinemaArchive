package com.example.cinemaarchive.data.database

import androidx.room.*
import com.example.cinemaarchive.data.entity.FavoriteMovieEntity

@Dao
interface FavoriteMovieDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(film: FavoriteMovieEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(films: List<FavoriteMovieEntity>)

    @Query("SELECT * FROM FavoriteTable WHERE id LIKE (:id)")
    fun getById(id: Int): FavoriteMovieEntity?

    @Query("SELECT * FROM FavoriteTable")
    fun getAll(): List<FavoriteMovieEntity>

    @Delete
    fun delete(film: FavoriteMovieEntity)

    @Query("DELETE FROM FavoriteTable")
    fun deleteAll()

    @Query("SELECT * FROM FavoriteTable LIMIT 1")
    fun getFilmIfExist(): FavoriteMovieEntity?
}