package com.example.cinemaarchive

import android.app.Application
import com.example.cinemaarchive.di.AppModule
import com.example.cinemaarchive.di.DaggerAppComponent

class App : Application() {

   override fun onCreate() {
       super.onCreate()
       instance = this

       val appComponent = DaggerAppComponent.builder()
           .appModule(AppModule(this))
           .build()
       appComponent.inject(this)
   }

   companion object {
       var instance: App? = null
           private set
   }
}