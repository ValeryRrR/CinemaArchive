package com.example.cinemaarchive.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cinemaarchive.domain.entity.Film
import com.example.cinemaarchive.domain.usecase.GetFavoriteListUseCase
import com.example.cinemaarchive.domain.usecase.UpdateFavoriteListUseCase

class FavoriteListViewModel(
    private val updateFavoriteListUseCase: UpdateFavoriteListUseCase,
    private val getFavoriteListUseCase: GetFavoriteListUseCase
): ViewModel() {

    private val _favoriteListLiveData = MutableLiveData<List<Film>>()
    val favoriteListLiveData: LiveData<List<Film>>
        get() = _favoriteListLiveData

    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String>
        get() = _errorLiveData

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

    fun updateFavoriteListByPosition(film: Film, isFavorite: Boolean, position: Int){
        updateFavoriteListUseCase.updateFavoriteListByPosition(film.id, isFavorite, position)
    }
}