package com.example.cinemaarchive.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.cinemaarchive.domain.entity.Film

@Entity(tableName = "FavoriteTable")
data class FavoriteMovieEntity(
    val id: Int,
    val name: String,
    val filmPoster: String?,
    var description: String,
    var voteAverage: Double,
    var genreIds: List<Int>,
    val releaseDate: String,
    var isFavorite: Boolean
){
    @PrimaryKey(autoGenerate = true)
    var dataBaseId: Long = 0L
}

fun FavoriteMovieEntity.toDomainFilm(): Film {
    return Film(
        id,
        name,
        filmPoster,
        description,
        voteAverage,
        genreIds,
        releaseDate,
        isFavorite
    )
}

fun FavoriteMovieEntity.toFilmDataEntity(): FilmDataEntity {
    return FilmDataEntity(
        id,
        name,
        filmPoster,
        description,
        voteAverage,
        genreIds,
        releaseDate,
        isFavorite
    )
}