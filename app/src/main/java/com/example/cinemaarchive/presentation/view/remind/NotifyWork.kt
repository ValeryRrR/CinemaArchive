package com.example.cinemaarchive.presentation.view.remind

import android.content.Context
import androidx.work.Data
import androidx.work.ListenableWorker.Result.success
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.cinemaarchive.domain.entity.Film

class NotifyWork(val context: Context, params: WorkerParameters) : Worker(context, params) {

    override fun doWork(): Result {
        val film = createFilmFromInputData(inputData)
        val notifier = Notifier()
        notifier.sendNotification(film.id, film, context)

        return success()
    }

    private fun createFilmFromInputData(data: Data): Film {
        return Film(
            data.getInt("id", 0),
            data.getString("name").toString(),
            data.getString("poster"),
            data.getString("description").toString(),
            data.getDouble("voteAverage", 0.0),
            data.getIntArray("genre")!!.toList(),
            data.getString("releaseDate").toString(),
            data.getBoolean("isFavorite", false)
        )
    }
}