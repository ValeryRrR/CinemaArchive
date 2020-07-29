package com.example.cinemaarchive.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cinemaarchive.domain.usecase.GetFavoriteListUseCase
import com.example.cinemaarchive.domain.usecase.UpdateFavoriteListUseCase
import javax.inject.Inject

class FavoriteViewModelFactory @Inject constructor(
    private val updateFavoriteListUseCase: UpdateFavoriteListUseCase,
    private val getFavoriteListUseCase: GetFavoriteListUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoriteListViewModel::class.java)) {
            return FavoriteListViewModel(
                updateFavoriteListUseCase,
                getFavoriteListUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}