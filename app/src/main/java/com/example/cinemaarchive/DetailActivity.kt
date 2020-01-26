package com.example.cinemaarchive

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import com.example.otushomework1.Film
import kotlinx.android.synthetic.main.activity_detail.*


class DetailActivity : AppCompatActivity() {

    private lateinit var film: Film

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        film = intent.getParcelableExtra(INTENT_EXTRA_NAME)

        fillFilmInformation(film)
        setListeners()
    }

    private fun fillFilmInformation(film: Film) {
        image_view_poster.setImageResource(film.filmPoster)
        film_name.text = film.name
        editText_comment.setText(film.comment)
        favorite_toggle_button.isChecked = film.isFavorite
    }

    override fun onBackPressed() {
        intent.putExtra(INTENT_EXTRA_NAME, film)
        setResult(RESULT_OK, intent)
        finish()
        super.onBackPressed()
    }

    private fun setListeners() {
        favorite_toggle_button.setOnClickListener {
            film.isFavorite = favorite_toggle_button.isChecked
        }

        editText_comment.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                film.comment = s.toString()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }
}