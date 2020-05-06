package com.example.cinemaarchive.domain.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Film(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val name: String,
    @SerializedName("poster_path") val filmPoster: String?,
    @SerializedName("overview") var description: String,
    @SerializedName("vote_average") var voteAverage: Double,
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