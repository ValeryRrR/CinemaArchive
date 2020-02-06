package com.example.cinemaarchive.detailfilm

import com.example.cinemaarchive.repository.Film

interface OnFilmDetailFragmentListener {
    fun onOpenDetailFragment(film: Film)
}