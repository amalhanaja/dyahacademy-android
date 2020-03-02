package com.amalcodes.dyahacademy.android.analytics

/**
 * @author: AMAL
 * Created On : 02/03/20
 */


interface Event {
    val name: String
    val properties: Map<String, Any?>
        get() = emptyMap()
}