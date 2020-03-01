package com.amalcodes.dyahacademy.android.core

import androidx.lifecycle.LifecycleOwner

/**
 * @author: AMAL
 * Created On : 01/03/20
 */


inline fun <reified T : Any> LifecycleOwner.autoCleared(
    noinline initializer: () -> T? = { null }
) = AutoClearedValue(this, initializer)