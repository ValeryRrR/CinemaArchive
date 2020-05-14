package com.example.cinemaarchive.data.entity

import android.os.Parcelable
import com.example.cinemaarchive.domain.entity.Film
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class FilmDataEntity(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val name: String,
    @SerializedName("poster_path") val filmPoster: String?,
    @SerializedName("overview") var description: String,
    @SerializedName("vote_average") var voteAverage: Double,
    var isFavorite: Boolean
) : Parcelable{
    override fun equals(other: Any?): Boolean {
        return if (other is FilmDataEntity)
            this.id == other.id
        else other == this
    }

    override fun hashCode(): Int {
        return id
    }
}

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