package com.amalcodes.dyahacademy.android.features.quiz

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.viewModelScope
import com.amalcodes.dyahacademy.android.analytics.Analytics
import com.amalcodes.dyahacademy.android.core.BaseViewModel
import com.amalcodes.dyahacademy.android.core.UIEvent
import com.amalcodes.dyahacademy.android.core.UIState
import com.amalcodes.dyahacademy.android.core.toUIState
import com.amalcodes.dyahacademy.android.domain.mapIndexedIfNotEmpty
import com.amalcodes.dyahacademy.android.features.quiz.usecase.GetQuizzesUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

@ExperimentalCoroutinesApi
class QuizViewModel(
    private val getQuizzesUseCase: GetQuizzesUseCase,
    analytics: Analytics
) : BaseViewModel(analytics) {

    private val questions: MutableList<QuestionViewEntity> = mutableListOf()

    private val currentIndex: MediatorLiveData<Int> = MediatorLiveData()

    private var isCorrection: Boolean = false

    init {
        _uiState.value = UIState.Initial
        _uiState.addSource(currentIndex) {
            _uiState.postValue(
                QuizUIState.Content(
                    questions,
                    questions.toListOfAnswerViewEntity(it),
                    it,
                    isCorrection
                )
            )
        }
    }

    override fun handleEventChanged(event: UIEvent) {
        when (event) {
            is QuizUIEvent.Fetch -> fetch(event.lessonId, event.initialAnswer)
            is QuizUIEvent.RetryFailure -> fetch(event.lessonId, event.initialAnswer)
            is QuizUIEvent.SetCurrentItem -> when (event.position) {
                -1, questions.count() -> return
                else -> currentIndex.postValue(event.position)
            }
            is QuizUIEvent.FillAnswer -> {
                questions[event.position].answer = event.answer
                currentIndex.postValue(event.position)
            }
            is QuizUIEvent.Finish -> finishQuiz()
            else -> event.unhandled()
        }
    }

    private fun finishQuiz() {
        val correctAnswer = questions.count { it.isCorrectAnswer() }
        val blankAnswer = questions.count { it.answer.isEmpty() }
        val score = (correctAnswer.toFloat() / questions.count().toFloat()) * 100
        val state = QuizUIState.Finished(
            summary = QuizSummaryViewEntity(
                correctAnswer = correctAnswer,
                wrongAnswer = questions.count() - correctAnswer - blankAnswer,
                blankAnswer = questions.count { it.answer.isEmpty() },
                score = score.toInt()
            ),
            answers = questions.toListOfAnswerViewEntity(0)
        )
        _uiState.postValue(state)
    }

    private fun List<QuestionViewEntity>.toListOfAnswerViewEntity(position: Int): List<AnswerViewEntity> {
        return mapIndexed { index, quiz ->
            AnswerViewEntity(
                id = index,
                answer = quiz.answer,
                isCurrent = position == index,
                isCorrect = quiz.isCorrectAnswer(),
                isCorrectionEnabled = isCorrection
            )
        }
    }

    private fun QuestionViewEntity.isCorrectAnswer(): Boolean = answerSelections
        .find { it.answerMark == answer }?.isCorrect ?: false

    @ExperimentalCoroutinesApi
    private fun fetch(lessonId: String, initialAnswers: List<AnswerViewEntity>? = null) {
        isCorrection = !initialAnswers.isNullOrEmpty()
        getQuizzesUseCase(GetQuizzesUseCase.Input(lessonId))
            .map { list ->
                list.mapIndexedIfNotEmpty { i, quiz ->
                    quiz.toQuizViewEntity(
                        answer = initialAnswers?.getOrNull(i)?.answer.orEmpty(),
                        showCorrection = isCorrection
                    )
                }
            }
            .onStart { _uiState.postValue(UIState.Loading) }
            .onEach {
                questions.clear()
                questions.addAll(it)
                currentIndex.postValue(0)
            }
            .catch { _uiState.postValue(it.toUIState()) }
            .launchIn(viewModelScope)
    }

}
