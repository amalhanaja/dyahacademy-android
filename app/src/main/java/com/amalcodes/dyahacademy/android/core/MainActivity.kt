package com.amalcodes.dyahacademy.android.core

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.amalcodes.dyahacademy.android.BuildConfig
import com.amalcodes.dyahacademy.android.R
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupDebugTools()
        nav_host_fragment_container?.findNavController()
            ?.addOnDestinationChangedListener { controller, destination, arguments ->
                supportActionBar?.title = arguments?.getString("label") ?: destination.label
                supportActionBar?.subtitle = arguments?.getString("subtitle")
                when (destination.id) {
                    R.id.courseListFragment -> {
                        supportActionBar?.setDisplayHomeAsUpEnabled(false)
                        supportActionBar?.setDisplayShowHomeEnabled(false)
                    }
                    else -> {
                        supportActionBar?.setDisplayHomeAsUpEnabled(true)
                        supportActionBar?.setDisplayShowHomeEnabled(true)
                    }
                }
            }
    }

    private fun setupDebugTools() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
