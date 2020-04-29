package com.example.cinemaarchive.domain.usecase

import android.util.Log
import com.example.cinemaarchive.data.entity.Film
import com.example.cinemaarchive.data.network.*
import com.example.cinemaarchive.data.repository.MovieRepositoryImp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetFilmsUseCase(
    private var theMovieDBService: TheMovieDBService,
    private var movieRepository: MovieRepositoryImp
) {

    fun getFilms(getFistPageCallback: GetFistPageCallback, page: Int){
        theMovieDBService.getListFilms(API_KEY, RU_LANG, page).
            enqueue(object: Callback<ResponseDataClass>{
                override fun onResponse(
                    call: Call<ResponseDataClass>,
                    response: Response<ResponseDataClass>
                ) {
                    if(response.isSuccessful){
                        movieRepository.addToCache(response.body()!!.results)
                        getFistPageCallback.onSuccess(movieRepository.cachedOrFakeFilms)
                    }
                }

                override fun onFailure(call: Call<ResponseDataClass>, t: Throwable) {
                    Log.e( "Failure loadNextPage: ", t.message)
                    getFistPageCallback.onError(t.message.toString())
                }
            })
    }

    interface GetFistPageCallback {
        fun onSuccess(films: List<Film>)
        fun onError(error: String)
    }
}