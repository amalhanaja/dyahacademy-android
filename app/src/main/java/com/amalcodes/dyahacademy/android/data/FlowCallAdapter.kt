package com.amalcodes.dyahacademy.android.data

import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * @author: AMAL
 * Created On : 26/02/20
 */


class FlowBodyCallAdapter<T>(
    private val responseType: Type
) : CallAdapter<T, Flow<T>> {
    override fun adapt(call: Call<T>): Flow<T> = flow {
        emit(suspendCancellableCoroutine { it.doNetworkCall(call) })
    }

    private fun CancellableContinuation<T>.doNetworkCall(
        call: Call<T>
    ) {
        call.enqueue(
            onFailure = { resumeWithException(it) },
            onResponse = { resume(it.body()!!) }
        )
        invokeOnCancellation { call.cancel() }
    }

    override fun responseType(): Type = responseType
}

class FlowResponseCallAdpater<T>(
    private val responseType: Type
) : CallAdapter<T, Flow<Response<T>>> {
    override fun adapt(call: Call<T>): Flow<Response<T>> = flow {
        emit(suspendCancellableCoroutine { it.doNetworkCall(call) })
    }

    private fun CancellableContinuation<Response<T>>.doNetworkCall(
        call: Call<T>
    ) {
        call.enqueue(
            onFailure = { resumeWithException(it) },
            onResponse = { response -> resume(response) }
        )
        invokeOnCancellation { call.cancel() }
    }

    override fun responseType(): Type = responseType
}

fun <T> Call<T>.enqueue(
    onResponse: (response: Response<T>) -> Unit,
    onFailure: (t: Throwable) -> Unit
) {
    val callback = object : Callback<T> {
        override fun onFailure(call: Call<T>, t: Throwable) = onFailure(t)
        override fun onResponse(call: Call<T>, response: Response<T>) = onResponse(response)
    }
    enqueue(callback)
}