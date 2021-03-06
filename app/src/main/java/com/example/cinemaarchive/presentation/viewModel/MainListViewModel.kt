package com.example.cinemaarchive.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cinemaarchive.domain.entity.Film
import com.example.cinemaarchive.domain.usecase.GetFilmCallback
import com.example.cinemaarchive.domain.usecase.GetFilmsUseCase
import com.example.cinemaarchive.domain.usecase.UpdateFavoriteListUseCase
import com.example.cinemaarchive.presentation.enam.LoadingStates
import com.example.cinemaarchive.presentation.utils.SingleEvent


class MainListViewModel(
    private val updateFavoriteListUseCase: UpdateFavoriteListUseCase,
    private val getFilmsUseCase: GetFilmsUseCase
): ViewModel() {

    private val pageStart = 1
    private var totalPages = 50
    private var currentPage = pageStart
    private var isLastPage = false

    private val _responseMutableLiveData = MutableLiveData<ArrayList<Film>>()
    val responseLiveData: LiveData<ArrayList<Film>>
        get() = _responseMutableLiveData

    private val _loadingStateLiveData = MutableLiveData<SingleEvent<LoadingStates>>()
    val loadingStateLiveData: LiveData<SingleEvent<LoadingStates>>
        get() = _loadingStateLiveData

    private val _errorLiveData = MutableLiveData<SingleEvent<String>>()
    val errorLiveData: LiveData<SingleEvent<String>>
        get() = _errorLiveData

    private val _errorLoadingNextPageLiveData = MutableLiveData<SingleEvent<String>>()
    val errorLoadingNextPageLiveData: LiveData<SingleEvent<String>>
        get() = _errorLoadingNextPageLiveData


    init {
        loadFistPage()
    }

    fun lastItemReached(){
        loadNextPage()
    }

    private fun loadNextPage() {
        if (currentPage <= totalPages)
            _loadingStateLiveData.value = SingleEvent(LoadingStates.LOADING)

        getFilmsUseCase.getFilms(object : GetFilmCallback {
            override fun onError(error: String) {
                _errorLoadingNextPageLiveData.value = SingleEvent(error)
                _loadingStateLiveData.value = SingleEvent(LoadingStates.LOADED)
            }

            override fun onSuccess(films: List<Film>?) {
                _responseMutableLiveData += films as ArrayList<Film>
                if (currentPage <= totalPages) {
                    _loadingStateLiveData.value = SingleEvent(LoadingStates.LOADED)
                } else isLastPage = true
                currentPage += 1
            }
        }, currentPage)
    }

    private fun loadFistPage(){
        getFilmsUseCase.getFilms(object : GetFilmCallback{
            override fun onError(error: String) {
                _errorLiveData.value = SingleEvent(error)
                _loadingStateLiveData.value = SingleEvent(LoadingStates.ERROR)
            }

            override fun onSuccess(films: List<Film>?) {
                _responseMutableLiveData.value = films as ArrayList<Film>
                currentPage += 1
            }
        }, currentPage)
    }

    fun onLikeBtnClicked(film: Film, position: Int, isFavorite: Boolean){
        updateFavoriteListUseCase.updateFavoriteList(film.id, isFavorite)
        _responseMutableLiveData.value?.get(position)?.isFavorite = isFavorite
    }

    fun swipeRefreshWasPulled(){
        currentPage = pageStart
        loadFistPage()
    }

    fun favoriteBtnWasUpdated(filmId: Int, isFavorite: Boolean){
        _responseMutableLiveData.value?.firstOrNull{it.id == filmId}?.isFavorite = isFavorite
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