package com.example.techorbit

import android.app.Application
import com.example.techorbit.koin.injection.appModule
import com.example.techorbit.koin.injection.viewmodelmodule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class TechorbitApplicaton : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@TechorbitApplicaton)
            modules(listOf(appModule, viewmodelmodule))
        }
    }
}