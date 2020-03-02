package com.amalcodes.dyahacademy.android.domain.model

/**
 * @author: AMAL
 * Created On : 01/03/20
 */


sealed class Failure(override val message: String? = null) : Error() {
    object NoInternet : Failure("no_internet")
    data class Unknown(override val cause: Throwable? = null) : Failure("unknown")
    object NoData : Failure("no_data")
    abstract class Feature(override val message: String? = null) : Failure()
}