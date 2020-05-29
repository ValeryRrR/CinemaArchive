package com.example.cinemaarchive.presentation.view.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.cinemaarchive.R
import com.example.cinemaarchive.data.network.loadImage
import com.example.cinemaarchive.domain.entity.Film
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.detail_fragment.*
import kotlinx.android.synthetic.main.detail_fragment_collapsing.*


const val FILM_DETAIL_FRAGMENT_TAG = "FILM_DETAIL_FRAGMENT"

class DetailFragment : Fragment() {

    private lateinit var iBottomNavOwner: IBottomNavOwner

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.detail_fragment_collapsing, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val film: Film = arguments?.getParcelable("filmDetail")!!
        fillFilmInformation(film)
        setLikeState(film.isFavorite)

        btnShare.setOnClickListener { share(film) }
        btnPlay.setOnClickListener { searchOnYoutube(film) }
        btnLike.setOnClickListener { favoriteBtnClicked(film) }
    }

    private fun fillFilmInformation(film: Film) {
        loadImage(film.filmPoster, image_view_poster_collapsing.context)
            ?.into(image_view_poster_collapsing)
        loadImage(film.filmPoster, image_view_poster.context)
            ?.into(image_view_poster)
        film_name.text = film.name
        film_description.text = film.description
        vote_average.text = film.voteAverage.toString()
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