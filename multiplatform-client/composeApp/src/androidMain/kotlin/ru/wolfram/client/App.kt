package ru.wolfram.client

import android.app.Application
import org.koin.android.ext.koin.androidContext
import ru.wolfram.client.di.initKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@App)
        }
    }
}