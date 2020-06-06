package com.example.cinemaarchive.presentation.view.remind

import android.util.Log
import com.example.cinemaarchive.presentation.utils.filmFromMap
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


const val TAG = "FirebaseMessage"

class MessagingService: FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val film = filmFromMap(remoteMessage.data)
        val notifier = Notifier()
        notifier.sendNotification(film.id, film, applicationContext)

        Log.d(TAG, "FirebaseMessage Notifier Body: " + remoteMessage.notification?.body)
    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
    }
}