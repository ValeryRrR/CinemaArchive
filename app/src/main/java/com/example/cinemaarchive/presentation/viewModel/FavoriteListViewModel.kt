package com.example.cinemaarchive.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cinemaarchive.App
import com.example.cinemaarchive.domain.entity.Film

class FavoriteListViewModel: ViewModel() {

    private val _favoriteListLiveData = MutableLiveData<List<Film>>()
    val favoriteListLiveData: LiveData<List<Film>>
        get() = _favoriteListLiveData

    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String>
        get() = _errorLiveData

    private val updateFavoriteListUseCase = App.instance!!.updateFavoriteListUseCase
    private val getFavoriteListUseCase = App.instance!!.getFavoriteListUseCase

    init {
        refreshFavoriteList()
    }

     fun refreshFavoriteList() {
         if (getFavoriteListUseCase.isFavoriteListEmpty())
            _errorLiveData.value = "Favorites is empty"
         else {
             _errorLiveData.value = ""
             _favoriteListLiveData.value = getFavoriteListUseCase.getFavoriteList()
         }
    }

    fun removeFavoriteFilm(film: Film){
        updateFavoriteListUseCase.updateFavoriteList(film.id, false)
    }
}