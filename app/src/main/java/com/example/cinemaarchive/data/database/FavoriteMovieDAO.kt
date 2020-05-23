package com.example.cinemaarchive.data.database

import androidx.room.*
import com.example.cinemaarchive.data.entity.FavoriteMovieEntity
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface FavoriteMovieDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(film: FavoriteMovieEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(films: List<FavoriteMovieEntity>)

    @Query("SELECT * FROM FavoriteTable WHERE id = (:id)")
    fun getById(id: Int): Single<FavoriteMovieEntity?>

    @Query("SELECT * FROM FavoriteTable")
    fun getAll(): Flowable<List<FavoriteMovieEntity>>

    @Query("SELECT id FROM FavoriteTable")
    fun getAllId(): List<Int>

    @Delete
    fun delete(film: FavoriteMovieEntity)

    @Query("DELETE FROM FavoriteTable")
    fun deleteAll()

    @Query("DELETE FROM FavoriteTable WHERE id =:filmId")
    fun deleteByFilmId(filmId: Int)

    @Query("SELECT * FROM FavoriteTable LIMIT 1")
    fun getFilmIfExist(): FavoriteMovieEntity?
}