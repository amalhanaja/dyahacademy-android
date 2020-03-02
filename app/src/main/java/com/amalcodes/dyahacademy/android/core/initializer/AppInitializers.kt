package com.amalcodes.dyahacademy.android.core.initializer

import android.app.Application

/**
 * @author: AMAL
 * Created On : 01/03/20
 */


class AppInitializers(
    private val initializers: Set<AppInitializer>
) {

    fun initialize(app: Application) = initializers.forEach { it.initialize(app) }

}