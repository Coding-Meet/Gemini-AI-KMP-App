package com.coding.meet.gaminiaikmp

import android.app.Application
import appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
class MainApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MainApplication)
            modules(appModule)
        }
    }

}