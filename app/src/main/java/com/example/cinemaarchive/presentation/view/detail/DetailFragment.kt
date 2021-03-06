package com.example.cinemaarchive.presentation.view.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.cinemaarchive.App
import com.example.cinemaarchive.R
import com.example.cinemaarchive.data.network.loadImage
import com.example.cinemaarchive.di.AppModule
import com.example.cinemaarchive.di.presentation.DaggerViewModelComponent
import com.example.cinemaarchive.domain.entity.Film
import com.example.cinemaarchive.presentation.view.remind.FILM_REMIND_FRAGMENT_TAG
import com.example.cinemaarchive.presentation.view.remind.ReminderFragment
import com.example.cinemaarchive.presentation.viewModel.DetailViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.detail_fragment.*
import kotlinx.android.synthetic.main.detail_fragment_collapsing.*
import javax.inject.Inject


const val FILM_DETAIL_FRAGMENT_TAG = "FILM_DETAIL_FRAGMENT"

class DetailFragment : Fragment() {

    @Inject
    lateinit var detailViewModelFactory: DetailViewModelFactory //= App.instance!!.detailViewModelFactory

    private lateinit var iBottomNavOwner: IBottomNavOwner
    private val detailViewModel: DetailViewModel by lazy {
        ViewModelProvider(this, detailViewModelFactory).get(DetailViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.detail_fragment_collapsing, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val component = DaggerViewModelComponent
            .builder()
            .appModule(AppModule(requireContext()))
            .build()
        component.inject(this)

        val film: Film = arguments?.getParcelable("filmDetail")!!
        fillFilmInformation(film)
        setLikeState(film.isFavorite)

        btnShare.setOnClickListener { share(film) }
        btnPlay.setOnClickListener { searchOnYoutube(film) }
        btnLike.setOnClickListener { favoriteBtnClicked(film) }
        btnRemind.setOnClickListener { showRemindFragment(film) }


        detailViewModel.genre.observe(viewLifecycleOwner, Observer{ genres ->
            val prefix = getString(R.string.genres)
            film_genre.text = genres.joinToString (
                prefix = prefix,
                separator = ", "
            )
        })
        detailViewModel.onViewCreated(film)
    }

    private fun showRemindFragment(film: Film) {
        val reminderFragment = ReminderFragment().newInstance(film)
        val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()

        fragmentTransaction
            .add(R.id.containerFragment, reminderFragment, FILM_REMIND_FRAGMENT_TAG)
            .addToBackStack(null)
            .commit()
    }

    private fun fillFilmInformation(film: Film) {
        loadImage(film.filmPoster, image_view_poster_collapsing.context)
            ?.into(image_view_poster_collapsing)
        loadImage(film.filmPoster, image_view_poster.context)
            ?.into(image_view_poster)
        film_name.text = film.name
        film_description.text = film.description
        vote_average.text = film.voteAverage.toString()
        releaseDateTV.text = getString(R.string.releaseDate, film.releaseDate)
    }

    override fun onStart() {
        super.onStart()
        iBottomNavOwner.getBottomBar().visibility = View.GONE
    }

    override fun onStop() {
        super.onStop()
        iBottomNavOwner.getBottomBar().visibility = View.VISIBLE
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is IBottomNavOwner) {
            iBottomNavOwner = context
        } else {
            throw RuntimeException(
                "$context must implement IBottomNavOwner"
            )
        }
    }

    fun newInstance(film: Film): DetailFragment {
        val myFragment = DetailFragment()
        val args = Bundle()
        args.putParcelable("filmDetail", film)
        myFragment.arguments = args
        return myFragment
    }

    private fun favoriteBtnClicked(film: Film) {
        film.isFavorite = !film.isFavorite
        setLikeState(film.isFavorite)
        //TODO not implemented updating favorites in data layer
    }

    private fun setLikeState(favorite: Boolean) {
        when(favorite) {
            true -> btnLike.setImageResource(R.drawable.ic_like_detail)
            false -> btnLike.setImageResource(R.drawable.ic_dislike_detail)
        }
    }

    private fun share(film: Film) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, film.name)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(
            sendIntent,
            getString(R.string.share)
        )
        startActivity(shareIntent)
    }

    private fun searchOnYoutube(film: Film) {
        val packageName = "com.google.android.youtube"
        if (isAppInstalled(packageName)) {
            val intent = Intent(Intent.ACTION_SEARCH)
            intent.setPackage(packageName)
            intent.putExtra("query", "${film.name} ${getString(R.string.trailer)}")
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }else{
            Snackbar.make(requireView(), getString(R.string.need_youtube), Snackbar.LENGTH_LONG).show()
        }
    }

    private fun isAppInstalled(packageName: String): Boolean {
        val mIntent: Intent? = requireActivity().packageManager.getLaunchIntentForPackage(packageName)
        return mIntent != null
    }
}