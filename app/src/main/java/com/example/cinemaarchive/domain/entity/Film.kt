package com.example.cinemaarchive.domain.entity

import android.os.Parcelable
import com.example.cinemaarchive.data.entity.FilmDataEntity
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Film(
    val id: Int,
    val name: String,
    val filmPoster: String?,
    var description: String,
    var voteAverage: Double,
    var isFavorite: Boolean
    ): Parcelable{

    override fun equals(other: Any?): Boolean {
        return if (other is Film)
            this.id == other.id
        else other == this
    }

    override fun hashCode(): Int {
        return id
    }

    constructor() : this(0, "", "", "", 0.0, false)
}