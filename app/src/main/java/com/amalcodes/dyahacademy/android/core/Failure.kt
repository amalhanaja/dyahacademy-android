package com.amalcodes.dyahacademy.android.core

/**
 * @author: AMAL
 * Created On : 01/03/20
 */


sealed class Failure {
    object NoInternet : Failure()
    object Unknown : Failure()
    object NoData : Failure()
    abstract class Feature : Failure()
}