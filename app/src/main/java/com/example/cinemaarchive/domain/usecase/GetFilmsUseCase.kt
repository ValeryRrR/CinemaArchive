package com.example.cinemaarchive.domain.usecase

import com.example.cinemaarchive.data.network.*
import com.example.cinemaarchive.domain.repository.MovieRepository

class GetFilmsUseCase(
    private var theMovieDBApi: TheMovieDBApi,
    private var movieRepository: MovieRepository
) {

    fun getFilms(getFilmsCallback: GetFilmCallback, page: Int){
        /*theMovieDBApi.getListFilms(API_KEY, RU_LANG, page).
            enqueue(object: Callback<ResponseDataClass>{
                override fun onResponse(
                    call: Call<ResponseDataClass>,
                    response: Response<ResponseDataClass>
                ) {
                    if(response.isSuccessful){
                        if(page == 1){
                            movieRepository.clearCache()
                        }
                        movieRepository.addToCache(response.body()!!.results)
                        getFilmsCallback.onSuccess(movieRepository.cachedOrFakeFilms)
                    }
                }

                override fun onFailure(call: Call<ResponseDataClass>, t: Throwable) {
                    getFilmsCallback.onError(t.message.toString())
                }
            })*/
        movieRepository.getFilms(getFilmsCallback, page)
    }

/*    interface GetFilmCallback {
        fun onSuccess(films: List<Film>)
        fun onError(error: String)
    }*/
}