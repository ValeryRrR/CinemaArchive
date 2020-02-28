package com.example.cinemaarchive.mainfilmlist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cinemaarchive.network.API_KEY
import com.example.cinemaarchive.network.ENG_LANG
import com.example.cinemaarchive.network.FilmApi
import com.example.cinemaarchive.network.ResponseDataClass
import com.example.cinemaarchive.repository.Film
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainFilmListFragmentViewModel: ViewModel() {

     val PAGE_START = 1
     var TOTAL_PAGES = 5
     var currentPage = PAGE_START

     val isLoading = false
     val isLastPage = false

    private val _response = MutableLiveData<List<Film>>()

    val response: LiveData<List<Film>>
        get() = _response

    init {
        getMainListFilms()
    }

    private fun getMainListFilms() {
        FilmApi.retrofitService.listFilms(API_KEY, ENG_LANG, PAGE_START).enqueue(object: Callback<ResponseDataClass> {
            override fun onFailure(call: Call<ResponseDataClass>, t: Throwable) {
                Log.i( "Failure enqueue: ", t.message)
            }

            override fun onResponse(call: Call<ResponseDataClass>, response: Response<ResponseDataClass>) {
                _response.value = response.body()?.results
            }
        })
    }

    /*fun loadNextPage(){
        FilmApi.retrofitService.listFilms(API_KEY, ENG_LANG, currentPage).enqueue(object: Callback<ResponseDataClass> {
            override fun onFailure(call: Call<ResponseDataClass>, t: Throwable) {
                Log.i( "Failure loadNextPage: ", t.message)
            }

            override fun onResponse(call: Call<ResponseDataClass>, response: Response<ResponseDataClass>) {
                (_response.value as ArrayList<Film>).addAll(response.body()?.results as Array<Film>)
            }
        })
    }*/
}