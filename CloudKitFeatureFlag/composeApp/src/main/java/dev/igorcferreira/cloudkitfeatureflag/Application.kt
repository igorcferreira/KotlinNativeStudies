package dev.igorcferreira.cloudkitfeatureflag

import android.app.Application
import dev.igorcferreira.cloudkitfeatureflag.di.DIHelper
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class Application: Application() {
    override fun onCreate() {
        super.onCreate()
        DIHelper.initKoin()
            .androidContext(applicationContext)
            .androidLogger()
    }
}
