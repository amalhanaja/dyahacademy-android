package com.amalcodes.dyahacademy.android.domain.usecase

import kotlinx.coroutines.flow.Flow

/**
 * @author: AMAL
 * Created On : 01/03/20
 */


interface UseCase<in Input, out Output> {
    operator fun invoke(input: Input): Flow<Output>
}