package com.example.cinemaarchive.presentation.view.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cinemaarchive.domain.usecase.GetGenresUseCase
import com.example.cinemaarchive.presentation.viewModel.DetailViewModel
import javax.inject.Inject

class DetailViewModelFactory @Inject constructor(
    private val getGenresUseCase: GetGenresUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(
                getGenresUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}