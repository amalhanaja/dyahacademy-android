package com.amalcodes.dyahacademy.android.domain

import com.amalcodes.dyahacademy.android.domain.model.Failure

/**
 * @author: AMAL
 * Created On : 01/03/20
 */


inline fun <T, R : Any> List<T>.mapIfNotEmpty(transform: (T) -> R): List<R> {
    if (isEmpty()) throw Failure.NoData
    return mapNotNull(transform)
}