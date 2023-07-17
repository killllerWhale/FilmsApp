package com.example.filmsapp.app

import android.app.Application
import com.example.filmsapp.di.appModule
import org.koin.core.context.startKoin
import androidx.appcompat.app.AppCompatDelegate
import com.example.filmsapp.dataPrefs.Prefs
import org.koin.android.ext.koin.androidContext
import org.koin.java.KoinJavaComponent.get

class App : Application() {

    companion object {
        lateinit var instance: App
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        startKoin {
            androidContext(this@App)
            modules(appModule)
        }

        val prefs: Prefs = get(Prefs::class.java)

        when (prefs.modeTheme) {
            1 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            2 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            else -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }
}
