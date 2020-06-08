package com.example.cinemaarchive.domain.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Film(
    val id: Int,
    val name: String,
    val filmPoster: String?,
    var description: String,
    var voteAverage: Double,
    val genreIds: List<Int>,
    val releaseDate: String,
    var isFavorite: Boolean
    ): Parcelable{

    constructor() : this(0, "", "", "",0.0, listOf(), "", false)
}