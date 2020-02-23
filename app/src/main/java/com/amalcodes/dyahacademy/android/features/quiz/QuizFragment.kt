package com.amalcodes.dyahacademy.android.features.quiz

import android.graphics.Rect
import android.os.Bundle
import android.widget.Toast
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
                is QuizUIState.HasData -> onHasDataState(it.data, it.answers)
                is QuizUIState.AnswerFilled -> onAnswerFilledState(it.answers)
                is QuizUIState.QuizFinished -> onQuizFinishedState(it)
            }
        }
    }

    private fun onQuizFinishedState(data: QuizUIState.QuizFinished) {
        toolbar_quiz?.iv_menu?.isGone = true
        Toast.makeText(requireContext(), "${data.score}", Toast.LENGTH_SHORT).show()
    }

    private fun onAnswerFilledState(answers: List<AnswerViewEntity>) {
        answerAdapter.submitList(answers)
    }

    private fun onHasDataState(
        data: QuizViewEntity,
        answers: List<AnswerViewEntity>
    ) {
        toolbar_quiz?.iv_menu?.isVisible = true
        Injector.markwon.setMarkdown(mtv_quiz_question, data.question)
        iv_quiz_question?.isGone = data.questionImageUrl == null
        data.questionImageUrl?.let { iv_quiz_question?.load(it) }
        answerAdapter.submitList(answers)
        answerSelectionAdapter.submitList(data.answerSelections.map {
            it.copy(isSelected = it.answerMark.toString() == answers[data.currentIndex].answer)
        })
        answerSelectionAdapter.setOnViewHolderClickListener { view, item ->
            when (view.id) {
                R.id.cl_item_answer -> {
                    require(item is AnswerSelectionViewEntity)
                    val newAnswer = data.answerSelections.map {
                        it.copy(isSelected = item == it)
                    }
                    viewModel.fillAnswer(newAnswer.find {
                        it.isSelected
                    }?.answerMark?.toString().orEmpty())
                    answerSelectionAdapter.submitList(newAnswer)
                }
            }
        }
        answerAdapter.setOnViewHolderClickListener { view, item ->
            when (view.id) {
                R.id.ll_answer -> {
                    require(item is AnswerViewEntity)
                    viewModel.setCurrentIndex(answers.indexOf(item))
                }
            }
        }
    }

    private fun onErrorState(throwable: Throwable) {
        Timber.e(throwable)
    }

    @ExperimentalCoroutinesApi
    private fun onInitialState() {
        confirmationDialogViewModel.viewEntity = ConfirmationDialogViewEntity(
            title = "Yakin nih sudah Selesai ?",
            description = "5 soal belum dijawab",
            cancelButton = "Tidak" to { _ ->
            },
            confirmButton = "Yakin" to { _ ->
                viewModel.finishQuiz()
            },
            drawableRes = R.drawable.ic_check_circle
        )
        viewModel.fetch(args.lessonId)
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
