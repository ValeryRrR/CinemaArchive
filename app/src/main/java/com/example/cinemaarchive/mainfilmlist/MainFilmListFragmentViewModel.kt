package com.example.cinemaarchive.mainfilmlist

import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cinemaarchive.network.API_KEY
import com.example.cinemaarchive.network.FilmApi
import com.example.cinemaarchive.network.RU_LANG
import com.example.cinemaarchive.network.ResponseDataClass
import com.example.cinemaarchive.repository.Film
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainFilmListFragmentViewModel: ViewModel() {

     val PAGE_START = 1
     var TOTAL_PAGES = 50
     var currentPage = PAGE_START

     var isLoading = false
     var isLastPage = false

    private val _responseMutableLiveData = MutableLiveData<List<Film>>()

    val responseLiveData: LiveData<List<Film>>
        get() = _responseMutableLiveData

    private val _responseNextPageMutableLiveData = MutableLiveData<List<Film>>()

    val responseNextPageLiveData: LiveData<List<Film>>
        get() = _responseNextPageMutableLiveData

    init {
        loadMainListFilms()
    }

     fun loadMainListFilms() {
        FilmApi.retrofitService.listFilms(API_KEY, RU_LANG, PAGE_START).enqueue(object: Callback<ResponseDataClass> {
            override fun onFailure(call: Call<ResponseDataClass>, t: Throwable) {
                Log.i( "Failure enqueue: ", t.message)
            }

            //en-US

            override fun onResponse(call: Call<ResponseDataClass>, response: Response<ResponseDataClass>) {
                _responseMutableLiveData.value = response.body()?.results
            }
        })
    }

    fun loadNextPage(){
        FilmApi.retrofitService.listFilms(API_KEY, RU_LANG, currentPage).enqueue(object: Callback<ResponseDataClass> {
            override fun onFailure(call: Call<ResponseDataClass>, t: Throwable) {
                Log.i( "Failure loadNextPage: ", t.message)
            }

            override fun onResponse(call: Call<ResponseDataClass>, response: Response<ResponseDataClass>) {
                _responseNextPageMutableLiveData.value = response.body()?.results
                isLoading = false
            }
        })
    }
}