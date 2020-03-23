package com.example.cinemaarchive.presentation.recycler.holders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import androidx.recyclerview.widget.RecyclerView
import com.example.cinemaarchive.data.entity.Film
import kotlinx.android.synthetic.main.item_film.view.*

class FilmViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    val filmPosterIv: ImageView = itemView.imageView_filmPoster
    val nameTv: TextView = itemView.textView_name
    val descriptionTv: TextView = itemView.textView_description
    val favoriteTbtn: ToggleButton = itemView.favorite_tgb
    val voteAverage: TextView = itemView.vote_average

    fun bind(
        item: Film,
        itemClickListener: (film: Film) -> Unit,
        likeClickListener: (film: Film, position: Int, isFavoriteChecked: Boolean) -> Unit
    ){
      //  filmPosterIv.setImageResource(item.filmPoster)
        nameTv.text = item.name
        descriptionTv.text = item.description
        favoriteTbtn.isChecked = item.isFavorite
        voteAverage.text = item.voteAverage.toString()

        itemView.setOnClickListener {
            itemClickListener(item)
        }
        favoriteTbtn.setOnClickListener{
            likeClickListener(item, adapterPosition, favoriteTbtn.isChecked)
        }
    }

}