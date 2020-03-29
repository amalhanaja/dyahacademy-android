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
import androidx.viewpager2.widget.ViewPager2
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

class QuizFragment : Fragment(), TrackScreen {

    @ExperimentalCoroutinesApi
    private val viewModel: QuizViewModel by koinViewModel()

    private val confirmationDialogViewModel: ConfirmationDialogViewModel by activityViewModels {
        ConfirmationDialogViewModel.Factory
    }

    override val screenName: String = "QuizFragment"

    private val questionAdapter: MultiAdapter by autoCleared { MultiAdapter() }

    private val answerAdapter: MultiAdapter by autoCleared { MultiAdapter() }

    private var binding: FragmentQuizBinding by autoCleared()

    private val args: QuizFragmentArgs by navArgs()

    @ExperimentalCoroutinesApi
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
            binding.globalMessage.root.isVisible = it is UIState.Failed
            when (it) {
                is UIState.Initial -> onInitialState()
                is UIState.Failed -> onFailedState(it.failure)
                is QuizUIState.Content -> onContentState(
                    it.question,
                    it.answers,
                    it.position,
                    it.isCorrection
                )
                is QuizUIState.Finished -> onQuizFinishedState(it)
            }
        }
    }

    private fun onQuizFinishedState(uiState: QuizUIState.Finished) {
        binding.toolbar.ivMenu.isGone = true
        val direction = QuizFragmentDirections.actionQuizFragmentToQuizSummaryFragment(
            lessonId = args.lessonId,
            quizSummary = uiState.summary,
            answers = uiState.answers.toTypedArray(),
            lessonTitle = args.label,
            showCorrection = args.showCorrection
        )
        findNavController().navigate(direction)
    }

    @ExperimentalCoroutinesApi
    private fun onContentState(
        question: List<QuestionViewEntity>,
        answers: List<AnswerViewEntity>,
        position: Int,
        isCorrection: Boolean
    ) {
        binding.toolbar.ivMenu.isVisible = !isCorrection
        questionAdapter.submitList(question)
        answerAdapter.submitList(answers)
        binding.ivQuizNext.setOnClickListener {
            viewModel.dispatch(QuizUIEvent.SetCurrentItem(position + 1))
        }
        binding.ivQuizPrev.setOnClickListener {
            viewModel.dispatch(QuizUIEvent.SetCurrentItem(position - 1))
        }
        answerAdapter.submitList(answers.mapIndexed { index, answerViewEntity ->
            answerViewEntity.copy(isCurrent = index == position)
        })
        binding.rvAnswers.smoothScrollToPosition(position)
        if (binding.pager.currentItem != position) {
            binding.pager.currentItem = position
        }
        if (!isCorrection) {
            questionAdapter.setOnViewHolderClickListener { view, item ->
                when (view.id) {
                    R.id.cl_item_answer -> {
                        require(item is AnswerSelectionViewEntity)
                        val newAnswer = question[position].answerSelections.find {
                            item == it
                        }?.answerMark.orEmpty()
                        viewModel.dispatch(QuizUIEvent.FillAnswer(newAnswer, position))
                    }
                }
            }
        }
        answerAdapter.setOnViewHolderClickListener { view, item ->
            when (view.id) {
                R.id.ll_answer -> {
                    require(item is AnswerViewEntity)
                    if (!item.isCurrent) {
                        viewModel.dispatch(QuizUIEvent.SetCurrentItem(answers.indexOf(item)))
                    }
                }
            }
        }
        updateConfirmationDialogViewEntity(answers)
    }

    @ExperimentalCoroutinesApi
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
            confirmButton = getString(R.string.text_Sure) to { _ -> viewModel.dispatch(QuizUIEvent.Finish) },
            drawableRes = R.drawable.ic_check_circle
        )
    }

    @ExperimentalCoroutinesApi
    private fun onFailedState(failure: Failure) {
        binding.globalMessage.btnGlobalMessage.isVisible = true
        binding.globalMessage.btnGlobalMessage.setOnClickListener {
            viewModel.dispatch(
                QuizUIEvent.RetryFailure(
                    lessonId = args.lessonId,
                    initialAnswer = args.answers?.toList(),
                    failure = failure
                )
            )
        }
        when (failure) {
            is Failure.Unknown -> {
                binding.globalMessage.btnGlobalMessage.text = getString(R.string.text_Try_Again)
                binding.globalMessage.tvGlobalMessageTitle.text =
                    getString(R.string.text_error_general)
                binding.globalMessage.tvGlobalMessageDescription.text =
                    getString(R.string.text_error_general_description)
                binding.globalMessage.ivGlobalMessage.setImageResource(R.drawable.il_unknown_error)
            }
            is Failure.NoInternet -> {
                binding.globalMessage.btnGlobalMessage.text = getString(R.string.text_Try_Again)
                binding.globalMessage.tvGlobalMessageTitle.text =
                    getString(R.string.text_error_general)
                binding.globalMessage.tvGlobalMessageDescription.text =
                    getString(R.string.text_error_general_description)
                binding.globalMessage.ivGlobalMessage.setImageResource(R.drawable.il_no_internet)
            }
        }
    }

    @ExperimentalCoroutinesApi
    private fun onInitialState() {
        viewModel.dispatch(QuizUIEvent.Fetch(args.lessonId, args.answers?.toList()))
    }

    @ExperimentalCoroutinesApi
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
        binding.rvAnswers.adapter = answerAdapter
        binding.rvAnswers.addItemDecoration(itemOffsetDecoration)
        binding.pager.adapter = questionAdapter
        binding.pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(pos: Int) {
                viewModel.dispatch(QuizUIEvent.SetCurrentItem(pos))
            }
        })
    }

}


@ExperimentalCoroutinesApi
private fun injectFeature() = loadFeature

@ExperimentalCoroutinesApi
private val loadFeature by lazy {
    loadKoinModules(quizModule)
}

@ExperimentalCoroutinesApi
private val quizModule = module {
    factory { GetQuizzesUseCase(get()) }
    viewModel { QuizViewModel(get(), get()) }
}