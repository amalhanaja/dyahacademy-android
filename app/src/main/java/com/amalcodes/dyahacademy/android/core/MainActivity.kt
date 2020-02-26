package com.amalcodes.dyahacademy.android.core

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import coil.util.CoilLogger
import com.amalcodes.dyahacademy.android.BuildConfig
import com.amalcodes.dyahacademy.android.R
import com.google.firebase.crashlytics.FirebaseCrashlytics
import io.noties.markwon.Markwon
import io.noties.markwon.image.coil.CoilImagesPlugin
import timber.log.Timber

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Injector.markwon = Markwon.builder(this)
            .usePlugin(CoilImagesPlugin.create(this))
            .build()
        setupDebugTools()
        setupProductionTools()
    }

    private fun setupProductionTools() {
        if (!BuildConfig.DEBUG) {
            Timber.plant(object : Timber.Tree() {
                override fun log(
                    priority: Int,
                    tag: String?,
                    message: String,
                    t: Throwable?
                ) {
                    when (priority) {
                        Log.ERROR -> t?.let {
                            FirebaseCrashlytics.getInstance().recordException(it)
                        }
                    }
                }
            })
        }
    }

    private fun setupDebugTools() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            CoilLogger.setEnabled(BuildConfig.DEBUG)
        }
    }
}
