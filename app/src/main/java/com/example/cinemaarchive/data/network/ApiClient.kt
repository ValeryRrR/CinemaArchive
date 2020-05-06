package com.example.cinemaarchive.data.network

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import com.example.cinemaarchive.R
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


const val BASE_URL = "https://api.themoviedb.org/3/"

/**
 *w300 - image size, supported other "w500", "w780", "w1280", "original"
 *
 */
const val BASE_URL_IMG = "https://image.tmdb.org/t/p/w300"
const val API_KEY = "4ad6223423726930ca824e38f157d79e"

const val ENG_LANG = "en-US"
const val RU_LANG = "ru-RU"


private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

object TheMovieDBmApi {
    val retrofitApi: TheMovieDBApi by lazy {
        retrofit.create(TheMovieDBApi::class.java)
    }
}

fun loadImage(posterPath: String?, context: Context): GlideRequest<Drawable?>? {
    return GlideApp
        .with(context)
        .load(getImageURL(posterPath))
        .fallback(R.drawable.gradient) //TODO create holder for films without albums
        .centerCrop()
}

private fun getImageURL(posterPath: String?): String?{
    if (posterPath == null){
        return null
    }
    return BASE_URL_IMG + posterPath
}

fun isThereInternetConnection(context: Context): Boolean {
    val isConnected: Boolean
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = connectivityManager.activeNetworkInfo
    isConnected = networkInfo != null
    return isConnected
}