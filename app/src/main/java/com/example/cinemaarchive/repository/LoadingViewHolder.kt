package com.example.cinemaarchive.repository

import android.view.View
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_loading.view.*

class LoadingViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {
    val mProgressBar: ProgressBar = itemView.load_more_progress
}
