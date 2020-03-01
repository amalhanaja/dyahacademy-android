package com.amalcodes.dyahacademy.android.features.quiz

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amalcodes.dyahacademy.android.core.UIState
import com.amalcodes.dyahacademy.android.core.toUIState
import com.amalcodes.dyahacademy.android.domain.mapIndexedIfNotEmpty
import com.amalcodes.dyahacademy.android.features.quiz.usecase.GetQuizzesUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

class QuizViewModel(
    private val getQuizzesUseCase: GetQuizzesUseCase
) : ViewModel() {

    private val quizzes: MutableList<QuizViewEntity> = mutableListOf()

    private val currentIndex: MediatorLiveData<Int> = MediatorLiveData()

    private var isCorrection: Boolean = false

    private val _uiState: MediatorLiveData<UIState> = MediatorLiveData()

    val uiState: LiveData<UIState> = _uiState

    init {
        _uiState.value = UIState.Initial
        _uiState.addSource(currentIndex) {
            val state = if (isCorrection) {
                QuizUIState.Correction(quizzes[it], quizzes.toListOfAnswerViewEntity())
            } else {
                QuizUIState.Default(quizzes[it], quizzes.toListOfAnswerViewEntity())
            }
            _uiState.postValue(state)
        }
    }

    fun fillAnswer(answerMark: String) {
        val currentIndexValue = currentIndex.value ?: 0
        quizzes[currentIndexValue].answer = answerMark
        _uiState.postValue(
            QuizUIState.Default(quizzes[currentIndexValue], quizzes.toListOfAnswerViewEntity())
        )
    }

    fun setCurrentIndex(index: Int) {
        currentIndex.postValue(index)
    }

    fun finishQuiz() {
        val correctAnswer = quizzes.count { it.isCorrectAnswer() }
        val blankAnswer = quizzes.count { it.answer.isEmpty() }
        val score = (correctAnswer.toFloat() / quizzes.count().toFloat()) * 100
        val state = QuizUIState.Finished(
            summary = QuizSummaryViewEntity(
                correctAnswer = correctAnswer,
                wrongAnswer = quizzes.count() - correctAnswer - blankAnswer,
                blankAnswer = quizzes.count { it.answer.isEmpty() },
                score = score.toInt()
            ),
            answers = quizzes.toListOfAnswerViewEntity()
        )
        _uiState.postValue(state)
    }

    fun next() {
        when (val currentIndexValue = currentIndex.value ?: 0) {
            quizzes.count() - 1 -> return
            else -> currentIndex.postValue(currentIndexValue + 1)
        }
    }

    fun prev() {
        when (val currentIndexValue = currentIndex.value ?: 0) {
            0 -> return
            else -> currentIndex.postValue(currentIndexValue - 1)
        }
    }

    private fun List<QuizViewEntity>.toListOfAnswerViewEntity(): List<AnswerViewEntity> {
        val currentIndexValue = currentIndex.value ?: 0
        return mapIndexed { index, quiz ->
            AnswerViewEntity(
                id = index,
                answer = quiz.answer,
                isCurrent = currentIndexValue == index,
                isCorrect = quiz.isCorrectAnswer(),
                isCorrectionEnabled = isCorrection
            )
        }
    }

    private fun QuizViewEntity.isCorrectAnswer(): Boolean = answerSelections
        .find { it.answerMark == answer }?.isCorrect ?: false

    @ExperimentalCoroutinesApi
    fun fetch(lessonId: String, initialAnswers: List<AnswerViewEntity>? = null) {
        isCorrection = !initialAnswers.isNullOrEmpty()
        getQuizzesUseCase(GetQuizzesUseCase.Input(lessonId))
            .map { list ->
                list.mapIndexedIfNotEmpty { i, quiz ->
                    quiz.toQuizViewEntity(
                        answer = initialAnswers?.getOrNull(i)?.answer.orEmpty(),
                        index = i,
                        showCorrection = isCorrection
                    )
                }
            }
            .onStart { _uiState.postValue(UIState.Loading) }
            .onEach { quizzes.addAll(it).also { setCurrentIndex(0) } }
            .catch { _uiState.postValue(it.toUIState()) }
            .launchIn(viewModelScope)
    }

}
