package com.example.cinemaarchive.faforitelist

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
import kotlinx.android.synthetic.main.favarite_list_fragment.*

const val FAVORITE_LIST_FRAGMENT_TAG = "FAVORITE_LIST_FRAGMENT"


class FavoriteListFragment : Fragment() {

    var mCallback: OnFilmDetailFragmentListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.favarite_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (Database.favoriteList.isEmpty())
            favorites_list_empty.text = getString(R.string.favorites_list_empty)
        else showRecyclerWithFilms(arguments?.getParcelableArrayList("favoriteList")!!)
    }


    private fun showRecyclerWithFilms(items: List<Film>) {
        favorite_list_recycler.adapter =
            FilmRecyclerAdapter(
                LayoutInflater.from(favorite_list_recycler.context),
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
                        if (!film.isFavorite) {
                            Database.favoriteList.remove(film)
                            favorite_list_recycler.adapter?.notifyItemRemoved(position)
                        }
                    }
                })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFilmDetailFragmentListener) {
            mCallback = context
        } else {
            throw RuntimeException(
                "$context must implement OnFilmDetailFragmentListener"
            )
        }
    }
}

