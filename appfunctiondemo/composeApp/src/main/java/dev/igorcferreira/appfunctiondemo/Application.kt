package dev.igorcferreira.appfunctiondemo

import android.app.Application
import dev.igorcferreira.appfunctiondemo.di.repositoryModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class Application: Application() {

    lateinit var koinApplication: KoinApplication

    override fun onCreate() {
        super.onCreate()

        koinApplication = startKoin {
            modules(repositoryModule)
        }

        koinApplication
            .androidLogger(Level.INFO)
            .androidContext(applicationContext)
    }
}
