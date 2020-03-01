package com.amalcodes.dyahacademy.android.domain.model

/**
 * @author: AMAL
 * Created On : 01/03/20
 */


sealed class Failure : Error() {
    object NoInternet : Failure()
    object Unknown : Failure()
    object NoData : Failure()
    abstract class Feature : Failure()
}