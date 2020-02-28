package com.example.cinemaarchive.mainfilmlist

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.cinemaarchive.R
import com.example.cinemaarchive.detailfilm.OnFilmDetailFragmentListener
import com.example.cinemaarchive.repository.Film
import com.example.cinemaarchive.repository.FilmRecyclerAdapter
import com.example.cinemaarchive.repository.database.Database
import kotlinx.android.synthetic.main.main_fragment.*


const val FILM_LIST_FRAGMENT_TAG = "FILM_LIST_FRAGMENT"

class FilmListFragment : Fragment() {

    private val viewModel: MainFilmListFragmentViewModel by lazy {
        ViewModelProvider(this).get(MainFilmListFragmentViewModel::class.java)
    }

    var mCallback: OnFilmDetailFragmentListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.response.observe(viewLifecycleOwner, Observer {
            showRecyclerWithFilms(it)
        })
    }


    private fun showRecyclerWithFilms(items: List<Film>) {
        main_fragment_recycler.adapter =
            FilmRecyclerAdapter(
                LayoutInflater.from(main_fragment_recycler.context),
                items,
                object :
                    FilmRecyclerAdapter.OnItemClickListener {
                    override fun onItemClicked(film: Film) {
                        mCallback?.onOpenDetailFragment(film)
                    }
                },
                object :
                    FilmRecyclerAdapter.OnLikeClickListener {
                    override fun onLikeClicked(film: Film, position: Int, isFavoriteChecked: Boolean) {
                        film.isFavorite = isFavoriteChecked
                        if (isFavoriteChecked)
                            Database.favoriteList.add(film)
                        else if (!isFavoriteChecked)
                            Database.favoriteList.remove(film)
                    }
                },
                context!!)
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

