package com.example.cinemaarchive.presentation.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.cinemaarchive.R
import com.example.cinemaarchive.presentation.view.detailfilm.OnFilmDetailFragmentListener
import com.example.cinemaarchive.data.entity.Film
import com.example.cinemaarchive.presentation.recycler.FilmRecyclerAdapter
import com.example.cinemaarchive.presentation.viewModel.MainListViewModel
import com.example.cinemaarchive.utils.PaginationScrollListener
import kotlinx.android.synthetic.main.main_fragment.*


const val FILM_LIST_FRAGMENT_TAG = "FILM_LIST_FRAGMENT"

class FilmListFragment : Fragment() {

    private lateinit var filmRecyclerAdapter: FilmRecyclerAdapter
    private lateinit var mCallback: OnFilmDetailFragmentListener
    private val viewModel: MainListViewModel by lazy {
        ViewModelProvider(this).get(MainListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecycler(ArrayList())

        mainSwiperefresh.setOnRefreshListener {
            main_progress.visibility = View.VISIBLE
            viewModel.loadFistList()
        }
        viewModel.responseLiveData.observe(viewLifecycleOwner, Observer {
            main_progress.visibility = View.GONE
            if(viewModel.isLoading)
              filmRecyclerAdapter.removeLoadingFooter()
            filmRecyclerAdapter.updateList(it)

            mainSwiperefresh.isRefreshing = false
        })
    }

    private fun initRecycler(items: List<Film>) {
        filmRecyclerAdapter =
            FilmRecyclerAdapter(
                items,
                { mCallback.onOpenDetailFragment(it) },
                { film: Film, position: Int, isFavoriteChecked: Boolean ->
                    viewModel.updateFavoriteList(film, isFavoriteChecked)
                }
            )
        mainFragmentRecycler.adapter = filmRecyclerAdapter

        mainFragmentRecycler.addOnScrollListener(object :
            PaginationScrollListener(mainFragmentRecycler.layoutManager!!) {
            override fun loadMoreItems() {
                if (viewModel.currentPage <= viewModel.TOTAL_PAGES)
                filmRecyclerAdapter.addLoadingFooter()
                else viewModel.isLastPage = true

                viewModel.loadNextPage()
            }

            override fun getTotalPageCount(): Int {
                return viewModel.TOTAL_PAGES
            }

            override fun isLastPage(): Boolean {
                return viewModel.isLastPage
            }

            override fun isLoading(): Boolean {
                return viewModel.isLoading
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

