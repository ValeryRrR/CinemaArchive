package com.example.cinemaarchive.filmlist

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.cinemaarchive.R
import com.example.cinemaarchive.detailfilm.OnFilmDetailFragmentListener
import com.example.cinemaarchive.repository.Film
import com.example.cinemaarchive.repository.FilmRecyclerAdapter
import com.example.cinemaarchive.repository.database.Database
import kotlinx.android.synthetic.main.favorite_fragment.*


const val FILM_LIST_FRAGMENT_TAG = "FILM_LIST_FRAGMENT"

class FilmListFragment : Fragment() {

    var mCallback: OnFilmDetailFragmentListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.favorite_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showRecyclerWithFilms(arguments?.getParcelableArrayList("filmList")!!)
    }


    private fun showRecyclerWithFilms(items: List<Film>) {
        favorite_fragment_recycler.adapter =
            FilmRecyclerAdapter(
                LayoutInflater.from(favorite_fragment_recycler.context),
                items,
                object :
                    FilmRecyclerAdapter.OnItemClickListener {
                    override fun onItemClicked(film: Film) {
                        mCallback?.onOpenDetailFragment(film)
                    }
                },
                object :
                    FilmRecyclerAdapter.OnLikeClickListener {
                    override fun onLikeClicked(film: Film, position: Int) {
                        if (film.isFavorite)
                            Database.favoriteList.add(film)
                        else if (!film.isFavorite)
                            Database.favoriteList.remove(film)
                    }
                })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFilmDetailFragmentListener) {
            mCallback = context
        } else {
            throw RuntimeException(
                "$context must implement FilmListFragment"
            )
        }
    }
}

