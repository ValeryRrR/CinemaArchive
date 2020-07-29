package com.example.cinemaarchive.data.network

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import com.example.cinemaarchive.R
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.*


const val BASE_URL = "https://api.themoviedb.org/3/"
const val BASE_URL_IMG = "https://image.tmdb.org/t/p/w300"
private var apiKey = "4ad6223423726930ca824e38f157d79e"
private var language = Locale.getDefault().toLanguageTag()
private val retrofit = Retrofit.Builder()
    .client(createOkHttpClient(apiKey, language))
    .addConverterFactory(GsonConverterFactory.create())
    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
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

fun isThereInternetConnection(context: Context): Boolean {
    val isConnected: Boolean
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = connectivityManager.activeNetworkInfo
    isConnected = networkInfo != null
    return isConnected
}

private fun createOkHttpClient(
    apiKey: String,
    language: String
): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
        .addInterceptor(getHeaderInterceptor(apiKey, language))
        .build()
}

private fun getHeaderInterceptor(
    apiKey: String,
    language: String
): Interceptor {
    return object : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {

            val url = chain.request()
                .url
                .newBuilder()
                .addQueryParameter("api_key", apiKey)
                .addQueryParameter("language", language)
                .build()

            val request =
                chain.request().newBuilder()
                    .url(url)
                    .build()
            return chain.proceed(request)
        }
    }
}

private fun getImageURL(posterPath: String?): String? {
    if (posterPath == null) {
        return null
    }
    return BASE_URL_IMG + posterPath
}
