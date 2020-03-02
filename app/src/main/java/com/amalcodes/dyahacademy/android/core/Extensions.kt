package com.amalcodes.dyahacademy.android.core

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStoreOwner
import com.amalcodes.dyahacademy.android.BuildConfig.FLAVOR
import com.amalcodes.dyahacademy.android.analytics.Analytics
import com.amalcodes.dyahacademy.android.analytics.Event
import com.amalcodes.dyahacademy.android.domain.model.Failure
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.koin.getViewModel
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.java.KoinJavaComponent.getKoin

/**
 * @author: AMAL
 * Created On : 01/03/20
 */


inline fun <reified T : Any> LifecycleOwner.autoCleared(
    noinline initializer: () -> T? = { null }
) = AutoClearedValue(this, initializer)

inline fun <reified T : ViewModel> ViewModelStoreOwner.koinViewModel(
    qualifier: Qualifier? = null,
    crossinline ownerProducer: () -> ViewModelStoreOwner = { this },
    noinline parameters: ParametersDefinition? = null
): Lazy<T> = lazy(LazyThreadSafetyMode.NONE) {
    getKoin().getViewModel(ownerProducer(), T::class, qualifier, parameters)
}

fun Throwable.toUIState(): UIState.Failed = UIState.Failed(
    if (this is Failure) this else Failure.Unknown()
)

val isProduction: Boolean
    get() = FLAVOR == "production"

inline fun <reified T> Flow<T>.trackEvent(analytics: Analytics): Flow<T> = onEach {
    if (it is Event) analytics.trackEvent(it)
}
