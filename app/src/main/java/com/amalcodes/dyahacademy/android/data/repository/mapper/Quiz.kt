package com.amalcodes.dyahacademy.android.data.repository.mapper

import com.amalcodes.dyahacademy.android.data.api.response.QuizResponse
import com.amalcodes.dyahacademy.android.domain.mapIndexedIfNotEmpty
import com.amalcodes.dyahacademy.android.domain.model.Quiz

/**
 * @author: AMAL
 * Created On : 01/03/20
 */


fun QuizResponse.toQuiz(): Quiz = Quiz(
    id = requireNotNull(id),
    question = requireNotNull(question),
    questionImageUrl = questionImageUrl.orEmpty(),
    answer = "",
    answers = answers.orEmpty().mapIndexedIfNotEmpty { index, response ->
        response.toAnswer(index.plus(65).toChar().toString())
    }
)