package com.example.cinemaarchive.presentation.utils

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class PaginationScrollListener protected constructor(layoutManager: RecyclerView.LayoutManager) :
    RecyclerView.OnScrollListener() {

    private val layoutManager: LinearLayoutManager = layoutManager as LinearLayoutManager

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val visibleItemCount: Int = layoutManager.childCount
        val totalItemCount: Int = layoutManager.itemCount
        val firstVisibleItemPosition: Int = layoutManager.findFirstVisibleItemPosition()
        if (!isLoading()) {
            if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
                && firstVisibleItemPosition >= 0
            ) {
                lastItemReached()
            }
        }
    }

    protected abstract fun lastItemReached()
    abstract fun isLoading(): Boolean
}