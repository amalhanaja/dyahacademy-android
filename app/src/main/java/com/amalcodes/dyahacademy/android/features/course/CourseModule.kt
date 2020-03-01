package com.amalcodes.dyahacademy.android.features.course

import com.amalcodes.dyahacademy.android.features.course.usecase.GetCoursesUseCase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

/**
 * @author: AMAL
 * Created On : 01/03/20
 */

fun injectFeature() = loadFeature

private val loadFeature by lazy {
    loadKoinModules(courseModule)
}

val courseModule = module {
    factory { GetCoursesUseCase(get()) }
    viewModel { CourseListViewModel(get()) }
}