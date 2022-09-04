package com.exwise.bookista

import android.app.Application
import org.koin.core.context.startKoin
import com.exwise.bookista.di.firebaseModule
import com.exwise.bookista.di.viewModelModule
import org.koin.android.ext.koin.androidContext

class BookistaApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        //Start Koin
        startKoin {
            androidContext(this@BookistaApplication)
            modules(viewModelModule, firebaseModule)
        }
    }
}