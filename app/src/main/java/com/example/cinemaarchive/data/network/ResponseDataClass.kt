package com.example.cinemaarchive.data.network

import android.os.Parcelable
import com.example.cinemaarchive.data.entity.FilmDataEntity
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize


@Parcelize
data class ResponseDataClass(
    @Json(name = "results") val results: List<FilmDataEntity>
): Parcelable