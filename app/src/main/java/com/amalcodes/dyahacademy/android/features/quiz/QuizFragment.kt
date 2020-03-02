package com.amalcodes.dyahacademy.android.features.quiz

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.text.bold
import androidx.core.text.color
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.api.load
import com.amalcodes.dyahacademy.android.R
import com.amalcodes.dyahacademy.android.analytics.TrackScreen
import com.amalcodes.dyahacademy.android.component.ConfirmationDialogViewEntity
import com.amalcodes.dyahacademy.android.component.ConfirmationDialogViewModel
import com.amalcodes.dyahacademy.android.core.*
import com.amalcodes.dyahacademy.android.databinding.FragmentQuizBinding
import com.amalcodes.dyahacademy.android.domain.model.Failure
import com.amalcodes.dyahacademy.android.features.quiz.usecase.GetQuizzesUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module
import timber.log.Timber

class QuizFragment : Fragment(), TrackScreen {

    private val viewModel: QuizViewModel by koinViewModel()

    private val confirmationDialogViewModel: ConfirmationDialogViewModel by activityViewModels {
        ConfirmationDialogViewModel.Factory
    }

    override val screenName: String = "QuizFragment"

    private val answerSelectionAdapter: MultiAdapter by autoCleared { MultiAdapter() }

    private val answerAdapter: MultiAdapter by autoCleared { MultiAdapter() }

    private var binding: FragmentQuizBinding by autoCleared()

    private val args: QuizFragmentArgs by navArgs()

    override fun onAttach(context: Context) {
        injectFeature()
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentQuizBinding.inflate(inflater, container, false)
            .also { binding = it }
            .root
    }

    @ExperimentalCoroutinesApi
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupView()
        viewModel.uiState.observe(viewLifecycleOwner) {
            binding.pb.isVisible = it is UIState.Loading
            binding.cgContent.isVisible = it is QuizUIState
            when (it) {
                is UIState.Initial -> onInitialState()
                is UIState.Failed -> onErrorState(it.failure)
                is QuizUIState.Default -> onHasDataState(it.quiz, it.answers)
                is QuizUIState.Correction -> onAnswerFilledState(it.quiz, it.answers)
                is QuizUIState.Finished -> onQuizFinishedState(it)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        confirmationDialogViewModel.viewEntity = null
    }

    private fun onQuizFinishedState(uiState: QuizUIState.Finished) {
        binding.toolbar.ivMenu.isGone = true
        val direction = QuizFragmentDirections.actionQuizFragmentToQuizSummaryFragment(
            lessonId = args.lessonId,
            quizSummary = uiState.summary,
            answers = uiState.answers.toTypedArray(),
            lessonTitle = args.label
        )
        findNavController().navigate(direction)
    }

    private fun onAnswerFilledState(
        quiz: QuizViewEntity,
        answers: List<AnswerViewEntity>
    ) {
        showQuizAndAnswersHolder(quiz, answers)
        updateConfirmationDialogViewEntity(answers)
        answerAdapter.submitList(answers)
    }

    private fun onHasDataState(
        quiz: QuizViewEntity,
        answers: List<AnswerViewEntity>
    ) {
        binding.toolbar.ivMenu.isVisible = true
        showQuizAndAnswersHolder(quiz, answers)
        answerSelectionAdapter.submitList(quiz.answerSelections.map {
            it.copy(isSelected = it.answerMark == answers[quiz.currentIndex].answer)
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
                    }?.answerMark.orEmpty())
                    answerSelectionAdapter.submitList(newAnswer)
                }
            }
        }
        updateConfirmationDialogViewEntity(answers)
    }

    private fun showQuizAndAnswersHolder(
        quiz: QuizViewEntity,
        answers: List<AnswerViewEntity>
    ) {
        Injector.markwon.setMarkdown(binding.mtvQuizQuestion, quiz.question)
        binding.ivQuizQuestion.isGone = quiz.questionImageUrl.isNullOrEmpty()
        answerSelectionAdapter.submitList(quiz.answerSelections.map {
            it.copy(isSelected = it.answerMark == answers[quiz.currentIndex].answer)
        })
        if (!quiz.questionImageUrl.isNullOrEmpty()) {
            binding.ivQuizQuestion.load(requireNotNull(quiz.questionImageUrl))
        }
        answerAdapter.submitList(answers)
        val index = answers.indexOfFirst { it.isCurrent }
        binding.rvAnswers.scrollToPosition(index)
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

    @ExperimentalCoroutinesApi
    private fun onErrorState(failure: Failure) {
        Timber.e(failure)
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
        binding.toolbar.ivBack.isVisible = true
        binding.toolbar.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.toolbar.ivMenu.setImageResource(R.drawable.ic_check)
        binding.toolbar.ivMenu.setOnClickListener {
            val direction = QuizFragmentDirections
                .actionGlobalConfirmationDialogFragment()
            findNavController().navigate(direction)
        }
        binding.toolbar.mtvToolbarTitle.text = args.label
        binding.rvAnswerSelection.isNestedScrollingEnabled = false
        binding.rvAnswerSelection.adapter = answerSelectionAdapter
        binding.rvAnswerSelection.addItemDecoration(itemOffsetDecoration)
        binding.rvAnswers.adapter = answerAdapter
        binding.rvAnswers.addItemDecoration(itemOffsetDecoration)
        binding.ivQuizNext.setOnClickListener { viewModel.next() }
        binding.ivQuizPrev.setOnClickListener { viewModel.prev() }
    }

}


private fun injectFeature() = loadFeature

private val loadFeature by lazy {
    loadKoinModules(quizModule)
}

private val quizModule = module {
    factory { GetQuizzesUseCase(get()) }
    viewModel { QuizViewModel(get()) }
}