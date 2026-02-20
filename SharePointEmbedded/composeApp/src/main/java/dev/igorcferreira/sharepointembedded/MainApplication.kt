package dev.igorcferreira.sharepointembedded

import android.app.Application
import dev.igorcferreira.sharepointembedded.di.DIHelper
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        DIHelper.initKoin()
            .androidContext(applicationContext)
            .androidLogger()
    }
}
