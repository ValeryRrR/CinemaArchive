package com.example.cinemaarchive.presentation.utils

import com.example.cinemaarchive.domain.entity.Film


fun filmFromMap(data: Map<String, String>): Film {
    return Film(
        data["id"]!!.toInt(),
        data["name"]!!,
        data["poster"],
        data["description"]!!,
        data["voteAverage"]!!.toDouble(),
        data["genre"]!!.split(",").map { it.toInt() },
        data["releaseDate"]!!,
        data["isFavorite"]!!.toBoolean()
    )
}