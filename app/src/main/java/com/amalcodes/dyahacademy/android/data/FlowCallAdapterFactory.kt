package com.amalcodes.dyahacademy.android.data

import kotlinx.coroutines.flow.Flow
import retrofit2.CallAdapter
import retrofit2.Response
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * @author: AMAL
 * Created On : 26/02/20
 */


class FlowCallAdapterFactory private constructor() : CallAdapter.Factory() {

    companion object {
        fun create() = FlowCallAdapterFactory()
    }

    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        if (getRawType(returnType) != Flow::class.java) {
            return null
        }
        check(returnType is ParameterizedType) {
            "Flow return type must be parameterized as Flow<Foo> or Flow<out Foo>"
        }
        val responseType = getParameterUpperBound(0, returnType)
        return when (getRawType(responseType)) {
            Response::class.java -> {
                check(responseType is ParameterizedType) {
                    "Response must be parameterized as Response<Foo> or Response<out Foo>"
                }
                FlowResponseCallAdpater<Any>(getParameterUpperBound(0, responseType))
            }
            else -> FlowBodyCallAdapter<Any>(responseType)
        }
    }

}