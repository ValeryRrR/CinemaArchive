package com.example.cinemaarchive.network


import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.NonNull
import com.example.cinemaarchive.R
import com.example.cinemaarchive.network.GlideOptions.bitmapTransform
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


private const val BASE_URL = "https://api.themoviedb.org/3/"

/**
 *w300 - image size, supported other "w500", "w780", "w1280", "original"
 *
 */
const val BASE_URL_IMG = "https://image.tmdb.org/t/p/w300"
const val API_KEY = "4ad6223423726930ca824e38f157d79e"

const val ENG_LANG = "en-US"
const val RU_LANG = "ru-RU"

//TODO move api_key, language, image size to interceptor
interface ThemoviedbService {
    @GET("discover/movie")
    fun listFilms(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") pageIndex: Int
    ): Call<ResponseDataClass>
}

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()


// TODO create mapper for data class
object FilmApi {
    val retrofitService: ThemoviedbService by lazy {
        retrofit.create(ThemoviedbService::class.java)
    }
}

fun loadImage(@NonNull posterPath: String, context: Context): GlideRequest<Drawable?>? {
    return GlideApp
        .with(context)
        .load(BASE_URL_IMG + posterPath)
        .centerCrop()
}

