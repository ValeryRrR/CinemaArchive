package com.example.cinemaarchive.data.entity

import android.os.Parcelable
import com.example.cinemaarchive.domain.entity.Film
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class FilmDataEntity(

    @SerializedName("id")
    val id: Int,

    @SerializedName("title")
    val name: String,

    @SerializedName("poster_path")
    val filmPoster: String?,

    @SerializedName("overview")
    var description: String,

    @SerializedName("vote_average")
    var voteAverage: Double,

    var isFavorite: Boolean

) : Parcelable

fun FilmDataEntity.toDomainFilm(): Film {
    return Film(
        id,
        name,
        filmPoster,
        description,
        voteAverage,
        isFavorite
    )
}

fun FilmDataEntity.toFilmDbEntity(): FilmDbEntity {
    return FilmDbEntity(
        id,
        name,
        filmPoster,
        description,
        voteAverage,
        isFavorite
    )
}

fun FavoriteMovieEntity.toFilmDbEntity(): FilmDbEntity {
    return FilmDbEntity(
        id,
        name,
        filmPoster,
        description,
        voteAverage,
        isFavorite
    )
}

fun FilmDataEntity.toFilmFavoriteEntity(): FavoriteMovieEntity {
    return FavoriteMovieEntity(
        id,
        name,
        filmPoster,
        description,
        voteAverage,
        isFavorite
    )
}
