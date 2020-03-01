package com.amalcodes.dyahacademy.android.data.graphql

import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.suspendCancellableCoroutine
import timber.log.Timber
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * @author: AMAL
 * Created On : 25/02/20
 */


fun <T> ApolloCall<T>.asFlow() = flow<Response<T>> {
    emit(
        suspendCancellableCoroutine { continuation ->
            clone().enqueue(
                object : ApolloCall.Callback<T>() {
                    override fun onResponse(response: Response<T>) {
                        continuation.resume(response)
                    }

                    override fun onFailure(e: ApolloException) {
                        continuation.resumeWithException(e)
                    }

                    override fun onStatusEvent(event: ApolloCall.StatusEvent) {
                        if (event == ApolloCall.StatusEvent.COMPLETED) {
                            continuation.cancel()
                        }
                    }
                }
            )
            continuation.invokeOnCancellation {
                Timber.e(it)
                this@asFlow.cancel()
            }
        }
    )
}