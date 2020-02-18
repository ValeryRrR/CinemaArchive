package com.example.cinemaarchive.detailfilm

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.cinemaarchive.R
import com.example.cinemaarchive.repository.Film
import kotlinx.android.synthetic.main.main_fragment.*
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
        setListeners(film)
    }

    private fun fillFilmInformation(film: Film) {
        image_view_poster_collapsing.setImageResource(film.filmPoster)
        image_view_poster.setImageResource(film.filmPoster)
        film_name.text = film.name
        editText_comment.setText(film.comment)
    }

    private fun setListeners(film: Film) {

        editText_comment.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                film.comment = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }


    var iBottomNavOwner: IBottomNavOwner? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        iBottomNavOwner?.getBottomBar()?.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
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
}