package com.amalcodes.dyahacademy.android.features.lesson

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.amalcodes.dyahacademy.android.R
import com.amalcodes.dyahacademy.android.analytics.TrackScreen
import com.amalcodes.dyahacademy.android.core.*
import com.amalcodes.dyahacademy.android.databinding.FragmentLessonListBinding
import com.amalcodes.dyahacademy.android.domain.model.Failure
import com.amalcodes.dyahacademy.android.domain.model.LessonType
import com.amalcodes.dyahacademy.android.features.lesson.usecase.GetLessonsUseCase
import com.amalcodes.ezrecyclerview.adapter.entity.ItemEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

class LessonListFragment : Fragment(), TrackScreen {

    @ExperimentalCoroutinesApi
    private val viewModel: LessonListViewModel by koinViewModel()

    private var binding: FragmentLessonListBinding by autoCleared()

    private val args: LessonListFragmentArgs by navArgs()

    private val adapter: MultiAdapter by autoCleared { MultiAdapter() }

    override val screenName: String = "LessonList"

    @ExperimentalCoroutinesApi
    override fun onAttach(context: Context) {
        super.onAttach(context)
        injectFeature()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = FragmentLessonListBinding.inflate(inflater, container, false)
        .also { binding = it }
        .root

    @ExperimentalCoroutinesApi
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupView()
        viewModel.uiState.observe(viewLifecycleOwner) {
            binding.pb.isVisible = it is UIState.Loading
            binding.rvLessons.isVisible = it is LessonListUIState.Content
            binding.globalMessage.root.isVisible = it is UIState.Failed
            when (it) {
                is UIState.Initial -> onInitialState()
                is UIState.Failed -> onFailedState(it.failure)
                is LessonListUIState.Content -> onContentState(it.lessons)
                is LessonListUIState.GoToQuiz -> onGoToQuiz(it.stateToRestore, it.lessonViewEntity)
                is LessonListUIState.GoToYoutube -> onGoToYoutube(
                    it.stateToRestore,
                    it.lessonViewEntity
                )
            }
        }
    }

    @ExperimentalCoroutinesApi
    private fun setupView() {
        binding.toolbar.mtvToolbarTitle.text = args.label
        binding.toolbar.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.toolbar.ivBack.isVisible = true
        binding.rvLessons.adapter = adapter
        binding.rvLessons.addItemDecoration(
            ItemOffsetDecoration { viewHolder, count ->
                val position = viewHolder.adapterPosition
                when (viewHolder) {
                    is LessonViewHolder,
                    is LessonGroupTitleViewHolder ->
                        Rect().apply {
                            left = resources.getDimensionPixelSize(R.dimen.spacing_4)
                            right = resources.getDimensionPixelSize(R.dimen.spacing_4)
                            top = when (position) {
                                0 -> resources.getDimensionPixelSize(R.dimen.spacing_6)
                                else -> resources.getDimensionPixelSize(R.dimen.spacing_2)
                            }
                            bottom = when (position) {
                                count - 1 -> resources.getDimensionPixelSize(R.dimen.spacing_6)
                                else -> resources.getDimensionPixelSize(R.dimen.spacing_2)
                            }
                        }
                    else -> Rect()
                }
            })
        adapter.setOnViewHolderClickListener { view, item ->
            when (view.id) {
                R.id.cl_item_lesson -> {
                    require(item is LessonViewEntity)
                    when (item.type) {
                        LessonType.YOUTUBE -> viewModel.dispatch(
                            LessonListUIEvent.GoToYoutube(item)
                        )
                        LessonType.QUIZ -> viewModel.dispatch(
                            LessonListUIEvent.GoToQuiz(item)
                        )
                        else -> throw IllegalStateException("unexpected LessonType: ${item.type}")
                    }
                }
            }
        }
    }

    @ExperimentalCoroutinesApi
    private fun onGoToYoutube(stateToRestore: UIState?, lessonViewEntity: LessonViewEntity) {
        viewModel.dispatch(UIEvent.RestoreUIState(stateToRestore))
        val direction = LessonListFragmentDirections
            .goToYoutubeLesson(
                youtubeUrl = lessonViewEntity.youtubeUrl.orEmpty(),
                label = lessonViewEntity.title
            )
        findNavController().navigate(direction)
    }

    @ExperimentalCoroutinesApi
    private fun onGoToQuiz(stateToRestore: UIState?, lessonViewEntity: LessonViewEntity) {
        viewModel.dispatch(UIEvent.RestoreUIState(stateToRestore))
        val direction = LessonListFragmentDirections
            .goToQuiz(
                label = lessonViewEntity.title,
                lessonId = lessonViewEntity.id,
                answers = null
            )
        findNavController().navigate(direction)
    }

    @ExperimentalCoroutinesApi
    private fun onInitialState() {
        viewModel.fetch(args.topicId)
    }

    @ExperimentalCoroutinesApi
    private fun onFailedState(failure: Failure) {
        binding.globalMessage.btnGlobalMessage.isVisible = true
        binding.globalMessage.btnGlobalMessage.setOnClickListener {
            viewModel.dispatch(LessonListUIEvent.RetryFailure(args.topicId, failure))
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

    private fun onContentState(lessons: List<ItemEntity>) {
        adapter.submitList(lessons)
    }
}

@ExperimentalCoroutinesApi
private fun injectFeature() = loadFeature

@ExperimentalCoroutinesApi
private val loadFeature by lazy {
    loadKoinModules(lessonListModule)
}

@ExperimentalCoroutinesApi
private val lessonListModule = module {
    factory { GetLessonsUseCase(get()) }
    viewModel { LessonListViewModel(get(), get()) }
}
