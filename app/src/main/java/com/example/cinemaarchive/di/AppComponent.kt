package com.example.cinemaarchive.di

import android.content.Context
import androidx.fragment.app.Fragment
import com.example.cinemaarchive.App
import com.example.cinemaarchive.di.data.DataModule
import com.example.cinemaarchive.di.domain.UseCasesModule
import com.example.cinemaarchive.di.presentation.ViewModelFactoryModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, DataModule::class, UseCasesModule::class, ViewModelFactoryModule::class])
interface AppComponent {
    fun inject(app: App)
    fun inject(fragment: Fragment)
    fun provideApplicationContext(): Context

}