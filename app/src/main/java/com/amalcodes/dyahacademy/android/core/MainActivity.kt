package com.amalcodes.dyahacademy.android.core

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.amalcodes.dyahacademy.android.BuildConfig
import com.amalcodes.dyahacademy.android.R
import timber.log.Timber

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupDebugTools()
    }

    private fun setupDebugTools() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
