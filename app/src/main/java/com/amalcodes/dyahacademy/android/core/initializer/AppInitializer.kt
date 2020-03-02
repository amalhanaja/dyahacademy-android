package com.amalcodes.dyahacademy.android.core.initializer

import android.app.Application

/**
 * @author: AMAL
 * Created On : 01/03/20
 */


interface AppInitializer {
    fun initialize(app: Application)
}