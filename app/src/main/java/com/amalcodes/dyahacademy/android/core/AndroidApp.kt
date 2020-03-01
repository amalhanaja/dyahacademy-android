package com.amalcodes.dyahacademy.android.core

import android.app.Application
import com.amalcodes.dyahacademy.android.BuildConfig
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

/**
 * @author: AMAL
 * Created On : 01/03/20
 */


class AndroidApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@AndroidApp)
            androidLogger(level = if (BuildConfig.DEBUG) Level.DEBUG else Level.NONE)
            modules(coreModules)
        }
    }
}