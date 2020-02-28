package com.example.cinemaarchive.repository

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Film(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val name: String,
    @SerializedName("poster_path") val filmPoster: String,
    @SerializedName("overview") var description: String,
    @SerializedName("vote_average") var voteAverage: Double,
    var isFavorite: Boolean
    ): Parcelable