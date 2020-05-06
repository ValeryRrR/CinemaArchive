package com.example.cinemaarchive.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cinemaarchive.data.database.Database
import com.example.cinemaarchive.data.database.Database.favoriteList
import com.example.cinemaarchive.domain.entity.Film

class FavoriteListViewModel: ViewModel() {

    private val _favoriteListLiveData = MutableLiveData<ArrayList<Film>>()
    val favoriteListLiveData: LiveData<ArrayList<Film>>
        get() = _favoriteListLiveData

    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String>
        get() = _errorLiveData


    init {
        refreshFavoriteList()
    }

     fun refreshFavoriteList() {
         if (favoriteList.isEmpty())
            _errorLiveData.value = "Favorites is empty"
         else _favoriteListLiveData.value = Database.favoriteList
    }

    fun removeFavoriteFilm(film: Film){
        favoriteList.remove(film) //todo move it
    }
}