package com.amalcodes.dyahacademy.android.core

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.amalcodes.dyahacademy.android.R
import io.noties.markwon.Markwon
import io.noties.markwon.image.coil.CoilImagesPlugin

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Injector.markwon = Markwon.builder(this)
            .usePlugin(CoilImagesPlugin.create(this))
            .build()
    }
}
