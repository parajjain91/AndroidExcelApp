package com.varunbehl.myinventory

import android.app.Application
import com.varunbehl.myinventory.networkLayer.ApiModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(
                listOf(
                    ApiModule
                )
            )
        }
    }
}