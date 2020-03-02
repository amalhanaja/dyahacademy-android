package com.amalcodes.dyahacademy.android.core.initializer

import android.app.Application
import android.util.Log
import com.amalcodes.dyahacademy.android.BuildConfig
import com.google.firebase.crashlytics.FirebaseCrashlytics
import timber.log.Timber

/**
 * @author: AMAL
 * Created On : 01/03/20
 */


class TimberInitializer : AppInitializer {
    override fun initialize(app: Application) {
        val tree = if (BuildConfig.DEBUG) Timber.DebugTree() else CrashReportingTree()
        Timber.plant(tree)
    }

    class CrashReportingTree : Timber.Tree() {
        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            when (priority) {
                Log.ERROR -> t?.let {
                    FirebaseCrashlytics.getInstance().recordException(it)
                }
            }
        }
    }

}