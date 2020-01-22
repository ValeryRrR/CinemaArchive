package com.example.otushomework1

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Film(var name: String, val description: String, var filmPoster: Int, var isFavorite: Boolean, var comment: String): Parcelable