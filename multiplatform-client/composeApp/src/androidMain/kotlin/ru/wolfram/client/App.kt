package ru.wolfram.client

import android.app.Application
import ru.wolfram.client.di.initKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin()
    }
}