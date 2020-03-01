package com.amalcodes.dyahacademy.android.data.repository

import com.amalcodes.dyahacademy.android.domain.repository.CourseRepository
import com.amalcodes.dyahacademy.android.domain.repository.QuizRepository
import org.koin.dsl.module

/**
 * @author: AMAL
 * Created On : 01/03/20
 */


val repositoryModule = module {
    single<CourseRepository> { CourseDataRepository(get()) }
    single<QuizRepository> { QuizDataRepository(get()) }
}