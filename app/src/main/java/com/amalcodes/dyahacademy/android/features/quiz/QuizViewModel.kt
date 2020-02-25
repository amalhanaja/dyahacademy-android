package com.amalcodes.dyahacademy.android.features.quiz

import androidx.lifecycle.*
import com.amalcodes.dyahacademy.android.features.lesson.LessonRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

class QuizViewModel : ViewModel() {

    private val _uiState: MediatorLiveData<QuizUIState> = MediatorLiveData()

    private val quizzes: MutableList<QuizViewEntity> = mutableListOf()

    private val currentIndex: MediatorLiveData<Int> = MediatorLiveData()

    private var isCorrection: Boolean = false

    val uiState: LiveData<QuizUIState> = _uiState

    init {
        _uiState.value = QuizUIState.Initial
        _uiState.addSource(currentIndex) {
            if (quizzes.isNotEmpty()) {
                val state = if (isCorrection) {
                    QuizUIState.AnswersChecked(quizzes[it], quizzes.toListOfAnswerViewEntity())
                } else {
                    QuizUIState.HasData(quizzes[it], quizzes.toListOfAnswerViewEntity())
                }
                _uiState.postValue(state)
            } else {
                _uiState.postValue(QuizUIState.Error(Error("No Data")))
            }
        }
    }

    fun fillAnswer(answerMark: String) {
        val currentIndexValue = currentIndex.value ?: 0
        quizzes[currentIndexValue].answer = answerMark
        _uiState.postValue(QuizUIState.AnswerFilled(quizzes.toListOfAnswerViewEntity()))
    }

    fun setCurrentIndex(index: Int) {
        currentIndex.postValue(index)
    }

    fun finishQuiz() {
        val correctAnswer = quizzes.count { it.isCorrectAnswer() }
        val blankAnswer = quizzes.count { it.answer.isEmpty() }
        val score = (correctAnswer.toFloat() / quizzes.count().toFloat()) * 100
        val state = QuizUIState.QuizFinished(
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

    private fun QuizViewEntity.isCorrectAnswer(): Boolean = answerSelections.find {
        it.answerMark.toString() == answer
    }?.isCorrect ?: false

    @ExperimentalCoroutinesApi
    fun fetch(lessonId: String, initialAnswers: List<AnswerViewEntity>? = null) {
        isCorrection = !initialAnswers.isNullOrEmpty()
        LessonRepository.getLessonById(lessonId)
            .onStart { _uiState.postValue(QuizUIState.Loading) }
            .catch { _uiState.postValue(QuizUIState.Error(it)) }
            .onEach { lesson ->
                quizzes.addAll(lesson.quizzes.orEmpty().mapIndexed { index, quiz ->
                    val answers = quiz.answers.orEmpty().mapIndexed { indexAnswer, answer ->
                        AnswerSelectionViewEntity(
                            text = answer.text.orEmpty(),
                            isCorrect = requireNotNull(answer.isCorrect),
                            answerMark = 65.plus(indexAnswer).toChar(),
                            isCorrectionEnabled = isCorrection
                        )
                    }
                    QuizViewEntity(
                        question = requireNotNull(quiz.question),
                        answerSelections = answers,
                        currentIndex = index,
                        count = lesson.quizzes.orEmpty().count(),
                        questionImageUrl = quiz.questionImageUrl.orEmpty(),
                        answer = initialAnswers?.getOrNull(index)?.answer.orEmpty()
                    )
                })
                currentIndex.postValue(0)
            }.launchIn(viewModelScope)
    }


    object Factory : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return QuizViewModel() as T
        }
    }


}
