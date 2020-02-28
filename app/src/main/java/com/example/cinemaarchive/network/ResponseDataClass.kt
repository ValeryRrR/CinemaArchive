package com.example.cinemaarchive.network

import android.os.Parcelable
import com.example.cinemaarchive.repository.Film
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize


@Parcelize
data class ResponseDataClass(
    @Json(name = "results") val results: List<Film>
): Parcelable