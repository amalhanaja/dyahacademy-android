package com.amalcodes.dyahacademy.android.features.quiz

import androidx.lifecycle.*
import com.amalcodes.dyahacademy.android.features.lesson.LessonRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class QuizViewModel : ViewModel() {

    private val _uiState: MediatorLiveData<QuizUIState> = MediatorLiveData()

    private val quizzes: MutableList<QuizViewEntity> = mutableListOf()

    private val currentIndex: MediatorLiveData<Int> = MediatorLiveData()

    val uiState: LiveData<QuizUIState> = _uiState

    init {
        _uiState.value = QuizUIState.Initial
        _uiState.addSource(currentIndex) {
            if (quizzes.isNotEmpty()) {
                _uiState.postValue(
                    QuizUIState.HasData(
                        quizzes[it],
                        quizzes.mapIndexed { index, quiz ->
                            AnswerViewEntity(
                                id = index,
                                answer = quiz.answer,
                                isCurrent = it == index
                            )
                        })
                )
            }
        }
    }

    fun fillAnswer(answerMark: String) {
        val currentIndexValue = currentIndex.value ?: 0
        quizzes[currentIndexValue].answer = answerMark
        _uiState.postValue(
            QuizUIState.AnswerFilled(quizzes.mapIndexed { index, quiz ->
                AnswerViewEntity(
                    id = index,
                    answer = quiz.answer,
                    isCurrent = currentIndexValue == index
                )
            })
        )
    }

    fun setCurrentIndex(index: Int) {
        currentIndex.postValue(index)
    }

    fun finishQuiz() {
        fun QuizViewEntity.isCorrectAnswer(): Boolean = answerSelections.find {
            it.answerMark.toString() == answer
        }?.isCorrect ?: false

        val correctAnswer = quizzes.count { it.isCorrectAnswer() }
        val score = (correctAnswer.toFloat() / quizzes.count().toFloat()) * 100
        val state = QuizUIState.QuizFinished(
            correctAnswer = correctAnswer,
            incorrectAnswer = quizzes.count { it.isCorrectAnswer() },
            blankAnswer = quizzes.count { it.answer.isEmpty() },
            score = score.toInt()
        )
        _uiState.postValue(state)
    }

    fun next() {
        currentIndex.postValue((currentIndex.value ?: -1) + 1)
    }

    fun prev() {
        currentIndex.postValue(currentIndex.value ?: 0 - 1)
    }

    @ExperimentalCoroutinesApi
    fun fetch(lessonId: String) {
        viewModelScope.launch {
            LessonRepository.getLessonById(lessonId)
                .catch { _uiState.postValue(QuizUIState.Error(it)) }
                .collect { lesson ->
                    quizzes.addAll(lesson.quizzes().orEmpty().mapIndexed { index, quiz ->
                        val answerData = quiz.answers() as List<*>
                        val answers = answerData.mapIndexed { indexAnswer, answer ->
                            require(answer is Map<*, *>)
                            AnswerSelectionViewEntity(
                                text = answer["text"] as String,
                                isCorrect = answer["isCorrect"] as Boolean,
                                answerMark = 65.plus(indexAnswer).toChar()
                            )
                        }
                        QuizViewEntity(
                            question = quiz.question(),
                            answerSelections = answers,
                            currentIndex = index,
                            count = lesson.quizzes().orEmpty().count(),
                            questionImageUrl = quiz.questionImageUrl(),
                            answer = ""
                        )
                    })
                    next()
                }
        }
    }


    object Factory : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return QuizViewModel() as T
        }
    }


}
