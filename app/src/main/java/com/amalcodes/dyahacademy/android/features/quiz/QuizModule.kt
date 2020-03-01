package com.amalcodes.dyahacademy.android.features.quiz

import com.amalcodes.dyahacademy.android.features.quiz.usecase.GetQuizzesUseCase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

/**
 * @author: AMAL
 * Created On : 01/03/20
 */


fun injectFeature() = loadFeature

private val loadFeature by lazy {
    loadKoinModules(quizModule)
}

val quizModule = module {
    factory { GetQuizzesUseCase(get()) }
    viewModel { QuizViewModel(get()) }
}