package com.amalcodes.dyahacademy.android.core

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * @author: AMAL
 * Created On : 01/03/20
 */


class AutoClearedValue<T : Any>(
    private val lifecycleOwner: LifecycleOwner,
    initializer: () -> T?
) : ReadWriteProperty<LifecycleOwner, T>, LifecycleObserver {

    private var _value: T? = null

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    internal fun onDestroy() {
        lifecycleOwner.lifecycle.removeObserver(this)
        _value = null
    }

    init {
        lifecycleOwner.lifecycle.addObserver(this)
        _value = initializer()
    }

    override fun getValue(
        thisRef: LifecycleOwner,
        property: KProperty<*>
    ): T = requireNotNull(_value) {
        "should never call auto-cleared-value get when it might not be available"
    }

    override fun setValue(thisRef: LifecycleOwner, property: KProperty<*>, value: T) {
        _value = value
    }
}
