package com.amalcodes.dyahacademy.android.core

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import coil.util.CoilLogger
import com.amalcodes.dyahacademy.android.BuildConfig
import com.amalcodes.dyahacademy.android.R
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
    }

    private fun setupDebugTools() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            CoilLogger.setEnabled(BuildConfig.DEBUG)
        }
    }
}
