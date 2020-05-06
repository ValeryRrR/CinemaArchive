package com.example.cinemaarchive.presentation.view.detailfilm

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.cinemaarchive.R
import com.example.cinemaarchive.domain.entity.Film
import com.example.cinemaarchive.data.network.loadImage
import kotlinx.android.synthetic.main.detail_fragment.*
import kotlinx.android.synthetic.main.detail_fragment_collapsing.*


const val FILM_DETAIL_FRAGMENT_TAG = "FILM_DETAIL_FRAGMENT"

class DetailFragment : Fragment() {



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
    }

    private fun fillFilmInformation(film: Film) {
        loadImage(film.filmPoster, image_view_poster_collapsing.context)?.into(image_view_poster_collapsing)//image_view_poster_collapsing.setImageResource(film.filmPoster)
        loadImage(film.filmPoster, image_view_poster.context)?.into(image_view_poster)//image_view_poster.setImageResource(film.filmPoster)
        film_name.text = film.name
        film_description.text = film.description
    }


    var iBottomNavOwner: IBottomNavOwner? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        iBottomNavOwner?.getBottomBar()?.visibility = View.GONE
    }

    override fun onPause() {
        super.onPause()
        iBottomNavOwner?.getBottomBar()?.visibility = View.VISIBLE
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
}