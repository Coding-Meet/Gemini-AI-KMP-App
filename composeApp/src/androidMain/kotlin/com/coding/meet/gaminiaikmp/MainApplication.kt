package com.coding.meet.gaminiaikmp

import android.app.Application
import com.coding.meet.gaminiaikmp.di.initKoin
import org.koin.android.ext.koin.androidContext

class MainApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@MainApplication)
        }
    }

}