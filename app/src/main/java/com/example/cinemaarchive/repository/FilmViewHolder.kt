package com.example.cinemaarchive.repository

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import androidx.recyclerview.widget.RecyclerView
import com.example.cinemaarchive.R
import com.example.cinemaarchive.repository.Film
import com.example.cinemaarchive.repository.FilmRecyclerAdapter
import kotlinx.android.synthetic.main.item_film.view.*

class FilmViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    val filmPosterIv: ImageView = itemView.imageView_filmPoster
    val nameTv: TextView = itemView.textView_name
    val descriptionTv: TextView = itemView.textView_description
    val favoriteTbtn: ToggleButton = itemView.favorite_tgb
    val voteAverage: TextView = itemView.vote_average

    fun bind(
        item: Film,
        itemClickListener: FilmRecyclerAdapter.OnItemClickListener,
        likeClickListener: FilmRecyclerAdapter.OnLikeClickListener
    ){
      //  filmPosterIv.setImageResource(item.filmPoster)
        nameTv.text = item.name
        descriptionTv.text = item.description
        favoriteTbtn.isChecked = item.isFavorite
        voteAverage.text = item.voteAverage.toString()

        itemView.setOnClickListener {
            itemClickListener.onItemClicked(item)
        }
        favoriteTbtn.setOnClickListener{
            likeClickListener.onLikeClicked(item, adapterPosition, favoriteTbtn.isChecked)
        }
    }

}