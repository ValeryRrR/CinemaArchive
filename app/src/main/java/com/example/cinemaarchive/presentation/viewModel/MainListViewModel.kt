package com.example.cinemaarchive.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cinemaarchive.App
import com.example.cinemaarchive.data.entity.Film
import com.example.cinemaarchive.data.database.Database
import com.example.cinemaarchive.domain.usecase.GetFilmsUseCase


class MainListViewModel: ViewModel() {

     val PAGE_START = 1
     var TOTAL_PAGES = 50
     var currentPage = PAGE_START

     var isLastPage = false

    private val getFilmsUseCase = App.instance!!.getFilmsUseCase

    private val _responseMutableLiveData = MutableLiveData<ArrayList<Film>>()
    val responseLiveData: LiveData<ArrayList<Film>>
        get() = _responseMutableLiveData

    private val _loadingStateLiveData = MutableLiveData<LoadingStates>()
    val loadingStateLiveData: LiveData<LoadingStates>
        get() = _loadingStateLiveData


    init {
        loadFistPage()
    }

    fun lastItemReached(){
        loadNextPage()
    }

    private fun loadNextPage() {
        if (currentPage <= TOTAL_PAGES)
            _loadingStateLiveData.value = LoadingStates.LOADING

        getFilmsUseCase.getFilms(object : GetFilmsUseCase.GetFistPageCallback{
            override fun onError(error: String) {
                _loadingStateLiveData.value = LoadingStates.ERROR
            }

            override fun onSuccess(films: List<Film>) {
                _responseMutableLiveData.value = films as ArrayList<Film>
                if (currentPage <= TOTAL_PAGES) {
                    _loadingStateLiveData.value = LoadingStates.LOADED
                } else isLastPage = true
                currentPage += 1
            }
        }, currentPage)
    }

    private fun loadFistPage(){
        getFilmsUseCase.getFilms(object : GetFilmsUseCase.GetFistPageCallback{
            override fun onError(error: String) {
                _loadingStateLiveData.value = LoadingStates.ERROR
            }

            override fun onSuccess(films: List<Film>) {
                _responseMutableLiveData.value = films as ArrayList<Film>
                currentPage += 1
            }
        }, currentPage)
    }

    fun updateFavoriteList(film: Film, isFavorite: Boolean){
        film.isFavorite = isFavorite
        if (isFavorite)
            Database.favoriteList.add(film)
        else if (!isFavorite)
            Database.favoriteList.remove(film)
    }

    fun refreshFistPage(){
        //TODO fix it
        currentPage = 1
        loadFistPage()
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

enum class LoadingStates() {
    LOADING, LOADED, ERROR
}