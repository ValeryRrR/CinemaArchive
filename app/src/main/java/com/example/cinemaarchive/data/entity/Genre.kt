package com.example.cinemaarchive.data.entity

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Genres(
    @Json(name = "genres") val genres: List<Genre>
): Parcelable

@Parcelize
data class Genre(
    val id: Int,
    val name: String
): Parcelable