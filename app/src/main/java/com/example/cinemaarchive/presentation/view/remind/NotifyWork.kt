package com.example.cinemaarchive.presentation.view.remind

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.graphics.Color.RED
import android.media.AudioAttributes
import android.media.AudioAttributes.CONTENT_TYPE_SONIFICATION
import android.media.AudioAttributes.USAGE_NOTIFICATION_RINGTONE
import android.media.RingtoneManager.TYPE_NOTIFICATION
import android.media.RingtoneManager.getDefaultUri
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.O
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.DEFAULT_ALL
import androidx.core.app.NotificationCompat.PRIORITY_MAX
import androidx.work.Data
import androidx.work.ListenableWorker.Result.success
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.cinemaarchive.R
import com.example.cinemaarchive.domain.entity.Film
import com.example.cinemaarchive.presentation.utils.vectorToBitmap
import com.example.cinemaarchive.presentation.view.MainActivity


class NotifyWork(context: Context, params: WorkerParameters) : Worker(context, params) {

    override fun doWork(): Result {
        val film = createFilmFromInputData(inputData)
        sendNotification(film.id, film)

        return success()
    }

    private fun sendNotification(id: Int, film: Film) {

        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
        intent.putExtra(REMIND_FILM, film)

        val notificationManager =
            applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val bitmap = applicationContext.vectorToBitmap(R.drawable.ic_slow_motion_video_24dp)
        val titleNotification = applicationContext.getString(R.string.notification_title)
        val pendingIntent = getActivity(applicationContext, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        val notification = NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL)
            .setLargeIcon(bitmap)
            .setSmallIcon(R.drawable.ic_slow_motion_video_24dp)
            .setContentTitle(titleNotification)
            .setContentText(film.name)
            .setDefaults(DEFAULT_ALL)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        notification.priority = PRIORITY_MAX

        if (SDK_INT >= O) {
            notification.setChannelId(NOTIFICATION_CHANNEL)

            val ringtoneManager = getDefaultUri(TYPE_NOTIFICATION)
            val audioAttributes = AudioAttributes.Builder().setUsage(USAGE_NOTIFICATION_RINGTONE)
                .setContentType(CONTENT_TYPE_SONIFICATION).build()

            val channel =
                NotificationChannel(NOTIFICATION_CHANNEL, NOTIFICATION_NAME, IMPORTANCE_HIGH)

            channel.enableLights(true)
            channel.lightColor = RED
            channel.enableVibration(true)
            channel.setSound(ringtoneManager, audioAttributes)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(id, notification.build())
    }

    private fun createFilmFromInputData(data: Data): Film {
        return Film(
            data.getInt("id", 0),
            data.getString("name")!!,
            data.getString("poster"),
            data.getString("description")!!,
            data.getDouble("voteAverage", 0.0),
            data.getBoolean("isFavorite", false)
        )
    }

    companion object {
        const val REMIND_FILM = "remindFilm"
        const val NOTIFICATION_ID = "CinemaArchive_notification_id"
        const val NOTIFICATION_NAME = "CinemaArchive"
        const val NOTIFICATION_CHANNEL = "CinemaArchive_channel_01"
        const val NOTIFICATION_WORK = "CinemaArchive_notification_work"
    }
}