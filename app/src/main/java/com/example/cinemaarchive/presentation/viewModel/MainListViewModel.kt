package com.example.cinemaarchive.presentation.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cinemaarchive.data.network.API_KEY
import com.example.cinemaarchive.data.network.FilmApi
import com.example.cinemaarchive.data.network.RU_LANG
import com.example.cinemaarchive.data.network.ResponseDataClass
import com.example.cinemaarchive.data.entity.Film
import com.example.cinemaarchive.data.database.Database
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainListViewModel: ViewModel() {

     val PAGE_START = 1
     var TOTAL_PAGES = 50
     var currentPage = PAGE_START

     var isLoading = false
     var isLastPage = false

    private val _responseMutableLiveData = MutableLiveData<List<Film>>()

    val responseLiveData: LiveData<List<Film>>
        get() = _responseMutableLiveData

    private val _responseNextPageMutableLiveData = MutableLiveData<ArrayList<Film>>()

    val responseNextPageLiveData: LiveData<ArrayList<Film>>
        get() = _responseNextPageMutableLiveData

    init {
        loadMainListFilms()
    }

     fun loadMainListFilms() {
        FilmApi.retrofitService.listFilms(API_KEY, RU_LANG, PAGE_START).enqueue(object: Callback<ResponseDataClass> {
            override fun onFailure(call: Call<ResponseDataClass>, t: Throwable) {
                Log.i( "Failure enqueue: ", t.message)
            }

            override fun onResponse(call: Call<ResponseDataClass>, response: Response<ResponseDataClass>) {
                _responseMutableLiveData.value = response.body()?.results
            }
        })
    }

    fun loadNextPage(){
        isLoading = true
        currentPage += 1

        FilmApi.retrofitService.listFilms(API_KEY, RU_LANG, currentPage).enqueue(object: Callback<ResponseDataClass> {
            override fun onFailure(call: Call<ResponseDataClass>, t: Throwable) {
                Log.i( "Failure loadNextPage: ", t.message)
            }

            override fun onResponse(call: Call<ResponseDataClass>, response: Response<ResponseDataClass>) {
                _responseNextPageMutableLiveData.value = response.body()?.results as ArrayList<Film>
                //_responseNextPageMutableLiveData += response.body()!!.results TODO save all list in LiveData
                isLoading = false
            }
        })
    }

    fun updateFavoriteList(film: Film, isFavorite: Boolean){
        film.isFavorite = isFavorite
        if (isFavorite)
            Database.favoriteList.add(film)
        else if (!isFavorite)
            Database.favoriteList.remove(film)
    }
}
    /**
    * Operator adding items in MutableLiveList like mutableLiveDataList += newMutableLiveDataList.
    */
private operator fun <T> MutableLiveData<ArrayList<T>>.plusAssign(values: List<T>) {
    val value = this.value ?: arrayListOf()
    value.addAll(values)
    this.value = value
}