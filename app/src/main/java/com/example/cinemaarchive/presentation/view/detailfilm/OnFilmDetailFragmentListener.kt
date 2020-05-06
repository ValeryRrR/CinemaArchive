package com.example.cinemaarchive.presentation.view.detailfilm

import com.example.cinemaarchive.domain.entity.Film

interface OnFilmDetailFragmentListener {
    fun onOpenDetailFragment(film: Film)
}