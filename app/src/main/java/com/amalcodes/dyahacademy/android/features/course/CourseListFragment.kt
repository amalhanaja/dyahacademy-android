package com.amalcodes.dyahacademy.android.features.course

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
import com.amalcodes.dyahacademy.android.R
import com.amalcodes.dyahacademy.android.analytics.TrackScreen
import com.amalcodes.dyahacademy.android.core.*
import com.amalcodes.dyahacademy.android.databinding.FragmentCourseListBinding
import com.amalcodes.dyahacademy.android.domain.model.Failure
import com.amalcodes.dyahacademy.android.features.course.usecase.GetCoursesUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

class CourseListFragment : Fragment(), TrackScreen {

    @ExperimentalCoroutinesApi
    private val viewModel: CourseListViewModel by koinViewModel()

    private val adapter by autoCleared { MultiAdapter() }
    private var binding: FragmentCourseListBinding by autoCleared()

    override val screenName: String = "CourseListFragment"

    @ExperimentalCoroutinesApi
    override fun onAttach(context: Context) {
        injectFeature()
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = FragmentCourseListBinding.inflate(inflater, container, false)
        .also { binding = it }
        .root

    @ExperimentalCoroutinesApi
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupView()
        viewModel.uiState.observe(viewLifecycleOwner) {
            binding.pbCourseList.isVisible = it is UIState.Loading
            binding.globalMessage.root.isVisible = it is UIState.Failed
            binding.rvCourseList.isVisible = it is CourseListUIState || it is UIState.Refreshing
            binding.refresh.isRefreshing = it is UIState.Refreshing
            when (it) {
                is UIState.Initial -> onInitialState()
                is UIState.Failed -> onErrorState(it.failure)
                is CourseListUIState.Content -> onContentState(it.list)
                is CourseListUIState.GoToTopics -> onGoToTopicsState(
                    it.stateToRestore,
                    it.courseViewEntity
                )
            }
        }
    }

    @ExperimentalCoroutinesApi
    private fun onGoToTopicsState(
        stateToRestore: UIState?,
        courseViewEntity: CourseViewEntity
    ) {
        viewModel.dispatch(UIEvent.RestoreUIState(stateToRestore))
        val directions = CourseListFragmentDirections
            .goToTopicList(
                courseId = courseViewEntity.course.id,
                courseTitle = courseViewEntity.course.title
            )
        findNavController().navigate(directions)
    }

    @ExperimentalCoroutinesApi
    private fun setupView() {
        binding.refresh.setOnRefreshListener { viewModel.dispatch(CourseListUIEvent.Refresh) }
        binding.toolbar.mtvToolbarTitle.text = getString(R.string.app_name)
        binding.rvCourseList.adapter = adapter
        binding.rvCourseList.addItemDecoration(ItemOffsetDecoration { viewHolder, count ->
            val position = viewHolder.adapterPosition
            if (viewHolder is CourseViewHolder) {
                return@ItemOffsetDecoration Rect().apply {
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
            }
            return@ItemOffsetDecoration Rect()
        })
        adapter.setOnViewHolderClickListener { view, item ->
            when (view.id) {
                R.id.cl_item_course_wrapper -> viewModel.dispatch(
                    CourseListUIEvent.GoToTopics(item as CourseViewEntity)
                )
            }
        }
    }

    @ExperimentalCoroutinesApi
    private fun onErrorState(failure: Failure) {
        binding.globalMessage.btnGlobalMessage.isVisible = true
        binding.globalMessage.btnGlobalMessage.setOnClickListener {
            viewModel.dispatch(CourseListUIEvent.RetryFailure(failure))
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

    private fun onContentState(data: List<CourseViewEntity>) {
        adapter.submitList(data)
    }

    @ExperimentalCoroutinesApi
    private fun onInitialState() {
        viewModel.dispatch(CourseListUIEvent.FetchCourses)
    }
}

@ExperimentalCoroutinesApi
private fun injectFeature() = loadFeature

@ExperimentalCoroutinesApi
private val loadFeature by lazy {
    loadKoinModules(courseModule)
}

@ExperimentalCoroutinesApi
private val courseModule = module {
    factory { GetCoursesUseCase(get()) }
    viewModel { CourseListViewModel(get(), get()) }
}
