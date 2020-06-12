package com.example.cinemaarchive.presentation.view

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.cinemaarchive.R
import com.example.cinemaarchive.di.AppModule
import com.example.cinemaarchive.di.presentation.DaggerViewModelComponent
import com.example.cinemaarchive.domain.entity.Film
import com.example.cinemaarchive.presentation.recycler.FilmRecyclerAdapter
import com.example.cinemaarchive.presentation.view.detail.OnFilmDetailFragmentListener
import com.example.cinemaarchive.presentation.viewModel.FavoriteListViewModel
import com.example.cinemaarchive.presentation.viewModel.FavoriteViewModelFactory
import com.example.cinemaarchive.presentation.viewModel.MainListViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.favarite_list_fragment.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class FavoriteListFragment : Fragment() {
    @Inject
    lateinit var favoriteViewModelFactory: FavoriteViewModelFactory

    private val viewModel: FavoriteListViewModel by lazy {
        ViewModelProvider(this, favoriteViewModelFactory).get(FavoriteListViewModel::class.java)
    }
    private var mCallback: OnFilmDetailFragmentListener? = null
    private lateinit var filmRecyclerAdapter: FilmRecyclerAdapter
    private val mainListViewModel: MainListViewModel by lazy {
        ViewModelProvider(requireActivity()).get(MainListViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.favarite_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val component = DaggerViewModelComponent
            .builder()
            .appModule(AppModule(requireContext()))
            .build()
        component.inject(this)

        initRecycler(ArrayList())

        viewModel.errorLiveData.observe(viewLifecycleOwner, Observer {
            favoritesListEmpty.text = it
        })

        viewModel.favoriteListLiveData.observe(viewLifecycleOwner, Observer {
            filmRecyclerAdapter.updateList(it)
        })
    }

    private fun initRecycler(items: List<Film>) {
        filmRecyclerAdapter = FilmRecyclerAdapter(
                items,
                { mCallback?.onOpenDetailFragment(it) },
                { film: Film, position: Int, isFavoriteChecked: Boolean ->
                    likeBtnClicked(film, position, isFavoriteChecked) }
            )
        favoriteListRecycler.adapter = filmRecyclerAdapter
    }

    private fun likeBtnClicked(film: Film, position: Int, isFavoriteChecked: Boolean) {
        filmRecyclerAdapter.onItemRemove(position, film)
        film.isFavorite = isFavoriteChecked
        showSnackBar(position, film, favoriteListRecycler)
        favoriteBtnWasUpdated(film.id, film.isFavorite)
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

    private fun showSnackBar(
        mAdapterPosition: Int,
        film: Film,
        view: View
    ) {
        val snackBar: Snackbar = Snackbar
            .make(
                view,
                R.string.remove_from_favorite,
                Snackbar.LENGTH_LONG
            )

        val timer = object: CountDownTimer(4000, 1000) {
            private val deleted = getString(R.string.deleted)
            private val sec = getString(R.string.sec)

            override fun onTick(millisUntilFinished: Long) {
                snackBar.setText(deleted + String.format(" %d $sec",
                    TimeUnit.MILLISECONDS.toSeconds( millisUntilFinished)))
            }

            override fun onFinish() {
                if (!film.isFavorite) {
                    viewModel.onLikeBtnClicked(film, film.isFavorite)
                    favoriteBtnWasUpdated(film.id, film.isFavorite)
                }
            }
        }


        snackBar.setAction(R.string.undo) {
            film.isFavorite = true
            filmRecyclerAdapter.onItemAdd(mAdapterPosition, film)
            favoriteBtnWasUpdated(film.id, film.isFavorite)
        }
        snackBar.show()
        timer.start()
    }

    private fun favoriteBtnWasUpdated(filmId: Int, isFavorite: Boolean){
        mainListViewModel.favoriteBtnWasUpdated(filmId, isFavorite)
    }
}

