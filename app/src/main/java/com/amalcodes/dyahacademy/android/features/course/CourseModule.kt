package com.amalcodes.dyahacademy.android.features.course

import com.amalcodes.dyahacademy.android.core.coreModules
import com.amalcodes.dyahacademy.android.features.course.usecase.GetCoursesUseCase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * @author: AMAL
 * Created On : 01/03/20
 */

val courseModule = module {
    factory { GetCoursesUseCase(get()) }
    viewModel { CourseListViewModel(get()) }
} + coreModules