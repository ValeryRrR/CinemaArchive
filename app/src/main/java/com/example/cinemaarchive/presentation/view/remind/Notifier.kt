package com.example.cinemaarchive.presentation.view.remind

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.cinemaarchive.R
import com.example.cinemaarchive.domain.entity.Film
import com.example.cinemaarchive.presentation.utils.vectorToBitmap
import com.example.cinemaarchive.presentation.view.MainActivity

class Notifier {

    fun sendNotification(id: Int, film: Film, context: Context) {

        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.putExtra(REMIND_FILM, film)

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val bitmap = context.vectorToBitmap(R.drawable.ic_slow_motion_video_24dp)
        val titleNotification = context.getString(R.string.notification_title)
        val pendingIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT)


        val notification = NotificationCompat.Builder(
            context,
            NOTIFICATION_CHANNEL
        )
            .setLargeIcon(bitmap)
            .setSmallIcon(R.drawable.ic_slow_motion_video_24dp)
            .setContentTitle(titleNotification)
            .setContentText(film.name)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        notification.priority = NotificationCompat.PRIORITY_MAX

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification.setChannelId(NOTIFICATION_CHANNEL)

            val ringtoneManager = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val audioAttributes =
                AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build()

            if (notificationManager.getNotificationChannel(NOTIFICATION_CHANNEL) == null) {
                val channel =
                    NotificationChannel(
                        NOTIFICATION_CHANNEL,
                        NOTIFICATION_NAME,
                        NotificationManager.IMPORTANCE_HIGH
                    )

                channel.enableLights(true)
                channel.lightColor = Color.RED
                channel.enableVibration(true)
                channel.setSound(ringtoneManager, audioAttributes)
                notificationManager.createNotificationChannel(channel)
            }
        }

        notificationManager.notify(id, notification.build())
    }

    companion object {
        const val REMIND_FILM = "remindFilm"
        const val NOTIFICATION_ID = "CinemaArchive_notification_id"
        const val NOTIFICATION_NAME = "CinemaArchive"
        const val NOTIFICATION_CHANNEL = "CinemaArchive_channel_01"
        const val NOTIFICATION_WORK = "CinemaArchive_notification_work"
    }
}