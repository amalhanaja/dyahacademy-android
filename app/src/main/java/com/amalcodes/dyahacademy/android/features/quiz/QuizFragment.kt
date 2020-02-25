package com.amalcodes.dyahacademy.android.features.quiz

import android.graphics.Rect
import android.os.Bundle
import android.text.SpannableStringBuilder
import androidx.core.content.res.ResourcesCompat
import androidx.core.text.bold
import androidx.core.text.color
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.api.load
import com.amalcodes.dyahacademy.android.R
import com.amalcodes.dyahacademy.android.component.ConfirmationDialogViewEntity
import com.amalcodes.dyahacademy.android.component.ConfirmationDialogViewModel
import com.amalcodes.dyahacademy.android.core.Injector
import com.amalcodes.dyahacademy.android.core.ItemOffsetDecoration
import com.amalcodes.dyahacademy.android.core.MultiAdapter
import kotlinx.android.synthetic.main.component_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_quiz.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber

class QuizFragment : Fragment(R.layout.fragment_quiz) {

    private val viewModel: QuizViewModel by viewModels(
        ownerProducer = { this },
        factoryProducer = {
            QuizViewModel.Factory
        }
    )

    private val confirmationDialogViewModel: ConfirmationDialogViewModel by activityViewModels {
        ConfirmationDialogViewModel.Factory
    }


    private val answerSelectionAdapter by lazy {
        MultiAdapter()
    }

    private val answerAdapter by lazy {
        MultiAdapter()
    }

    private val args: QuizFragmentArgs by navArgs()

    @ExperimentalCoroutinesApi
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupView()
        viewModel.uiState.observe(viewLifecycleOwner) {
            when (it) {
                is QuizUIState.Initial -> onInitialState()
                is QuizUIState.Error -> onErrorState(it.throwable)
                is QuizUIState.HasData -> onHasDataState(it.quiz, it.answers)
                is QuizUIState.AnswerFilled -> onAnswerFilledState(it.answers)
                is QuizUIState.QuizFinished -> onQuizFinishedState(it)
                is QuizUIState.AnswersChecked -> onAnswerCheckedState(it.quiz, it.answers)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        confirmationDialogViewModel.viewEntity = null
    }

    private fun onQuizFinishedState(uiState: QuizUIState.QuizFinished) {
        toolbar_quiz?.iv_menu?.isGone = true
        val direction = QuizFragmentDirections.actionQuizFragmentToQuizSummaryFragment(
            lessonId = args.lessonId,
            quizSummary = uiState.summary,
            answers = uiState.answers.toTypedArray(),
            lessonTitle = args.label
        )
        findNavController().navigate(direction)
    }

    private fun onAnswerFilledState(answers: List<AnswerViewEntity>) {
        updateConfirmationDialogViewEntity(answers)
        answerAdapter.submitList(answers)
    }

    private fun onAnswerCheckedState(
        quiz: QuizViewEntity,
        answers: List<AnswerViewEntity>
    ) {
        toolbar_quiz?.iv_menu?.isGone = true
        showQuizAndAnswersHolder(quiz, answers)
    }

    private fun onHasDataState(
        quiz: QuizViewEntity,
        answers: List<AnswerViewEntity>
    ) {
        toolbar_quiz?.iv_menu?.isVisible = true
        showQuizAndAnswersHolder(quiz, answers)
        answerSelectionAdapter.submitList(quiz.answerSelections.map {
            it.copy(isSelected = it.answerMark.toString() == answers[quiz.currentIndex].answer)
        })
        answerSelectionAdapter.setOnViewHolderClickListener { view, item ->
            when (view.id) {
                R.id.cl_item_answer -> {
                    require(item is AnswerSelectionViewEntity)
                    val newAnswer = quiz.answerSelections.map {
                        it.copy(isSelected = item == it)
                    }
                    viewModel.fillAnswer(newAnswer.find {
                        it.isSelected
                    }?.answerMark?.toString().orEmpty())
                    answerSelectionAdapter.submitList(newAnswer)
                }
            }
        }
        iv_quiz_next?.setOnClickListener { viewModel.next() }
        iv_quiz_prev?.setOnClickListener { viewModel.prev() }
        updateConfirmationDialogViewEntity(answers)
    }

    private fun showQuizAndAnswersHolder(
        quiz: QuizViewEntity,
        answers: List<AnswerViewEntity>
    ) {
        Injector.markwon.setMarkdown(mtv_quiz_question, quiz.question)
        iv_quiz_question?.isGone = quiz.questionImageUrl.isNullOrEmpty()
        answerSelectionAdapter.submitList(quiz.answerSelections.map {
            it.copy(isSelected = it.answerMark.toString() == answers[quiz.currentIndex].answer)
        })
        if (!quiz.questionImageUrl.isNullOrEmpty()) {
            iv_quiz_question?.load(requireNotNull(quiz.questionImageUrl))
        }
        answerAdapter.submitList(answers)
        val index = answers.indexOfFirst { it.isCurrent }
        rv_answers?.scrollToPosition(index)
        answerAdapter.setOnViewHolderClickListener { view, item ->
            when (view.id) {
                R.id.ll_answer -> {
                    require(item is AnswerViewEntity)
                    viewModel.setCurrentIndex(answers.indexOf(item))
                }
            }
        }
    }

    private fun updateConfirmationDialogViewEntity(answers: List<AnswerViewEntity>) {
        val blankAnswerCount = answers.count { it.answer.isEmpty() }
        confirmationDialogViewModel.viewEntity = ConfirmationDialogViewEntity(
            title = getString(R.string.text_finish_quiz_confirmation_dialog_title),
            description = SpannableStringBuilder().bold {
                color(ResourcesCompat.getColor(resources, R.color.coral, null)) {
                    append(blankAnswerCount.toString())
                }
            }.append(" ").append(getString(R.string.text_finish_quiz_confirmation_dialog_description_blank_answer)),
            cancelButton = getString(R.string.text_No) to { _ -> Unit },
            confirmButton = getString(R.string.text_Sure) to { _ -> viewModel.finishQuiz() },
            drawableRes = R.drawable.ic_check_circle
        )
    }

    private fun onErrorState(throwable: Throwable) {
        Timber.e(throwable)
    }

    @ExperimentalCoroutinesApi
    private fun onInitialState() {
        viewModel.fetch(args.lessonId, args.answers?.toList())
    }

    private fun setupView() {
        val itemOffsetDecoration = ItemOffsetDecoration { viewHolder, count ->
            val position = viewHolder.adapterPosition
            when (viewHolder) {
                is AnswerSelectionViewHolder -> Rect().apply {
                    left = resources.getDimensionPixelSize(R.dimen.spacing_6)
                    right = resources.getDimensionPixelSize(R.dimen.spacing_6)
                    top = when (position) {
                        0 -> resources.getDimensionPixelSize(R.dimen.spacing_4)
                        else -> resources.getDimensionPixelSize(R.dimen.spacing_1)
                    }
                    bottom = when (position) {
                        count - 1 -> resources.getDimensionPixelSize(R.dimen.spacing_4)
                        else -> resources.getDimensionPixelSize(R.dimen.spacing_1)
                    }
                }
                is AnswerViewHolder -> Rect().apply {
                    top = resources.getDimensionPixelSize(R.dimen.spacing_4)
                    right = resources.getDimensionPixelSize(R.dimen.spacing_0_5)
                    left = resources.getDimensionPixelSize(R.dimen.spacing_0_5)
                    bottom = resources.getDimensionPixelSize(R.dimen.spacing_2)
                }
                else -> Rect()
            }
        }
        toolbar_quiz?.iv_back?.isVisible = true
        toolbar_quiz?.iv_menu?.setImageResource(R.drawable.ic_check)
        toolbar_quiz?.iv_back?.setOnClickListener {
            findNavController().navigateUp()
        }
        toolbar_quiz?.iv_menu?.setOnClickListener {
            val direction = QuizFragmentDirections
                .actionGlobalConfirmationDialogFragment()
            findNavController().navigate(direction)
        }
        toolbar_quiz?.mtv_toolbar_title?.text = args.label
        rv_answer_selection?.isNestedScrollingEnabled = false
        rv_answer_selection?.adapter = answerSelectionAdapter
        rv_answer_selection?.addItemDecoration(itemOffsetDecoration)
        rv_answers?.adapter = answerAdapter
        rv_answers?.addItemDecoration(itemOffsetDecoration)
    }

}
