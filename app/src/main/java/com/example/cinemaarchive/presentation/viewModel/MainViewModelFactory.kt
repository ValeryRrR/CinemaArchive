package com.example.cinemaarchive.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cinemaarchive.domain.usecase.GetFilmsUseCase
import com.example.cinemaarchive.domain.usecase.UpdateFavoriteListUseCase

class MainViewModelFactory(
    private val updateFavoriteListUseCase: UpdateFavoriteListUseCase,
    private val getFilmsUseCase: GetFilmsUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainListViewModel::class.java)) {
            return MainListViewModel(
                updateFavoriteListUseCase,
                getFilmsUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}