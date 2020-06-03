package com.example.cinemaarchive.presentation.view.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cinemaarchive.domain.entity.Film
import com.example.cinemaarchive.domain.usecase.GetGenresUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class DetailViewModel(
    private val getGenresUseCase: GetGenresUseCase
) : ViewModel() {

    private val _genreLiveData = MutableLiveData<List<String>>()
    val genre: LiveData<List<String>>
        get() = _genreLiveData

    private val compositeDisposable = CompositeDisposable()


    fun onViewCreated(film: Film) {
        getGenres(film.genreIds)
    }

    private fun getGenres(ids: List<Int>) {
        compositeDisposable.add(
            getGenresUseCase.getGenres()
                .subscribeOn(Schedulers.io())
                .map {
                    it.genres.filter { genre ->
                        ids.contains(genre.id)
                    }
                }
                .map {
                    it.map { genre ->
                        genre.name
                    }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        _genreLiveData.value = it
                    },
                    {
                        it.printStackTrace()
                    }
                )
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}