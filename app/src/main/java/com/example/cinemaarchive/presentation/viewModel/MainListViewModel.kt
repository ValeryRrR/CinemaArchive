package com.example.cinemaarchive.presentation.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cinemaarchive.App
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

//    private val movieInteractor = App.instance!!.movieInteractor
    private val _responseMutableLiveData = MutableLiveData<ArrayList<Film>>()
    val responseLiveData: LiveData<ArrayList<Film>>
        get() = _responseMutableLiveData



    init {
        loadFistList()
    }

     fun loadNextPage() {
         isLoading = true

        FilmApi.retrofitService.getListFilms(API_KEY, RU_LANG, currentPage).enqueue(object: Callback<ResponseDataClass> {
            override fun onFailure(call: Call<ResponseDataClass>, t: Throwable) {
                Log.i( "Failure enqueue: ", t.message)
            }

            override fun onResponse(call: Call<ResponseDataClass>, response: Response<ResponseDataClass>) {
                _responseMutableLiveData += response.body()!!.results
                currentPage += 1
                isLoading = false
            }
        })
    }

    fun loadFistList(){

        FilmApi.retrofitService.getListFilms(API_KEY, RU_LANG, PAGE_START).enqueue(object: Callback<ResponseDataClass> {
            override fun onFailure(call: Call<ResponseDataClass>, t: Throwable) {
                Log.i( "Failure loadNextPage: ", t.message)
            }

            override fun onResponse(call: Call<ResponseDataClass>, response: Response<ResponseDataClass>) {
                _responseMutableLiveData.setValue(response.body()!!.results as ArrayList<Film>)

                currentPage += 1
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