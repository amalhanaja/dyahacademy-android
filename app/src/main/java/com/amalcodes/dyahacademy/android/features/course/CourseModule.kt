package com.amalcodes.dyahacademy.android.features.course

import com.amalcodes.dyahacademy.android.features.course.usecase.GetCoursesUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

/**
 * @author: AMAL
 * Created On : 01/03/20
 */

@ExperimentalCoroutinesApi
fun injectFeature() = loadFeature

@ExperimentalCoroutinesApi
private val loadFeature by lazy {
    loadKoinModules(courseModule)
}

@ExperimentalCoroutinesApi
val courseModule = module {
    factory { GetCoursesUseCase(get()) }
    viewModel { CourseListViewModel(get(), get()) }
}