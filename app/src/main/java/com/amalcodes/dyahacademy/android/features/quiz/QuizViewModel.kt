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
                _uiState.postValue(QuizUIState.HasData(quizzes[it]))
            }
        }
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
                        val answers = answerData.map { answer ->
                            require(answer is Map<*, *>)
                            AnswerViewEntity(
                                text = answer["text"] as String,
                                isCorrect = answer["isCorrect"] as Boolean
                            )
                        }
                        QuizViewEntity(
                            question = quiz.question(),
                            answers = answers,
                            current = index + 1,
                            count = lesson.quizzes().orEmpty().count()
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
