package com.example.cinemaarchive.data.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "FilmDbEntity", indices = [Index("id", unique = true )])
data class FilmDbEntity(
    val id: Int,
    val name: String,
    val filmPoster: String?,
    var description: String,
    var voteAverage: Double,
    var isFavorite: Boolean

){
    @PrimaryKey(autoGenerate = true)
    var dataBaseId: Long = 0L
}

fun FilmDbEntity.toFilmDataEntity(): FilmDataEntity {
    return FilmDataEntity(
        id,
        name,
        filmPoster,
        description,
        voteAverage,
        isFavorite
    )
}
