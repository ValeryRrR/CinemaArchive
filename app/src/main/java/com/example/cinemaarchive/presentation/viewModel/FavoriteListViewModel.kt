package com.example.cinemaarchive.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cinemaarchive.domain.entity.Film
import com.example.cinemaarchive.domain.usecase.GetFavoriteListUseCase
import com.example.cinemaarchive.domain.usecase.UpdateFavoriteListUseCase
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable

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

    private lateinit var disposable: Flowable<List<Film>>
    private val compositeDisposable = CompositeDisposable()

    init {
        createDisposable()
    }

    private fun createDisposable() {
        disposable = getFavoriteListUseCase.getFavoriteList()
        compositeDisposable.add(
            disposable.subscribe{
            updateFavoriteList(it)
        })
    }

    private fun updateFavoriteList(favoriteList: List<Film>){
        if(favoriteList.isEmpty()){
            _errorLiveData.value = "Favorites is empty"
        }else {
            _errorLiveData.value = ""}
        _favoriteListLiveData.value = favoriteList
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun onLikeBtnClicked(film: Film, isFavorite: Boolean){
        updateFavoriteListUseCase.updateFavoriteList(film.id, isFavorite)
    }
}