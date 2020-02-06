package com.example.cinemaarchive.repository

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Film(val id: Int, var name: String, val description: String, var filmPoster: Int, var isFavorite: Boolean, var comment: String): Parcelable