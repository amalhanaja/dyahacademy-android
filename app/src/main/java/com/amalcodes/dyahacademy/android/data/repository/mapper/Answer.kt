package com.amalcodes.dyahacademy.android.data.repository.mapper

import com.amalcodes.dyahacademy.android.data.api.response.AnswerResponse
import com.amalcodes.dyahacademy.android.domain.model.Answer

/**
 * @author: AMAL
 * Created On : 01/03/20
 */


fun AnswerResponse.toAnswer(mark: String) = Answer(
    text = requireNotNull(text),
    isCorrect = requireNotNull(isCorrect),
    mark = mark
)