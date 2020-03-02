package com.amalcodes.dyahacademy.android.features.topic

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.amalcodes.dyahacademy.android.R
import com.amalcodes.dyahacademy.android.analytics.TrackScreen
import com.amalcodes.dyahacademy.android.core.*
import com.amalcodes.dyahacademy.android.databinding.FragmentTopicListBinding
import com.amalcodes.dyahacademy.android.domain.model.Failure
import com.amalcodes.dyahacademy.android.features.topic.usecase.GetTopicsUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

class TopicListFragment : Fragment(), TrackScreen {

    @ExperimentalCoroutinesApi
    private val viewModel: TopicListViewModel by koinViewModel()

    private val adapter: MultiAdapter by autoCleared { MultiAdapter() }

    private var binding: FragmentTopicListBinding by autoCleared()

    private val args: TopicListFragmentArgs by navArgs()

    override val screenName: String = "TopicListFragment"

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
        return FragmentTopicListBinding.inflate(inflater, container, false)
            .also { binding = it }
            .root
    }

    @ExperimentalCoroutinesApi
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupView()
        viewModel.uiState.observe(viewLifecycleOwner) {
            binding.globalMessage.root.isVisible = it is UIState.Failed
            binding.rvTopics.isVisible = it is TopicListUIState.Content
            binding.pb.isVisible = it is UIState.Loading
            when (it) {
                is UIState.Initial -> onInitialState()
                is UIState.Failed -> onFailedState(it.failure)
                is TopicListUIState.Content -> onContentState(it.topics)
                is TopicListUIState.GoToLessons -> onGoToLessonsState(
                    it.stateToRestore,
                    it.topicViewEntity
                )
            }
        }
    }

    @ExperimentalCoroutinesApi
    private fun onGoToLessonsState(stateToRestore: UIState?, topicViewEntity: TopicViewEntity) {
        viewModel.dispatch(UIEvent.RestoreUIState(stateToRestore))
        val direction: NavDirections =
            TopicListFragmentDirections.goToLessonList(
                label = topicViewEntity.title,
                topicId = topicViewEntity.id
            )
        findNavController().navigate(direction)
    }

    @ExperimentalCoroutinesApi
    private fun setupView() {
        binding.toolbar.ivBack.isVisible = true
        binding.toolbar.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.toolbar.mtvToolbarTitle.text = args.courseTitle
        binding.rvTopics.adapter = adapter
        binding.rvTopics.addItemDecoration(
            ItemOffsetDecoration { viewHolder, count ->
                val position = viewHolder.adapterPosition
                if (viewHolder is TopicViewHolder) {
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
            }
        )
        adapter.setOnViewHolderClickListener { view, item ->
            when (view.id) {
                R.id.fl_item_topic -> viewModel.dispatch(
                    TopicListUIEvent.GoToLessons(item as TopicViewEntity)
                )
            }
        }
    }

    private fun onContentState(topics: List<TopicViewEntity>) {
        adapter.submitList(topics)
    }

    @ExperimentalCoroutinesApi
    private fun onFailedState(failure: Failure) {
        binding.globalMessage.btnGlobalMessage.isVisible = true
        binding.globalMessage.btnGlobalMessage.setOnClickListener {
            viewModel.dispatch(TopicListUIEvent.RetryFailure(args.courseId, failure))
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
        viewModel.dispatch(TopicListUIEvent.Fetch(args.courseId))
    }

}

@ExperimentalCoroutinesApi
private fun injectFeature() = loadFeature

@ExperimentalCoroutinesApi
private val loadFeature by lazy {
    loadKoinModules(topicsModule)
}

@ExperimentalCoroutinesApi
private val topicsModule = module {
    factory { GetTopicsUseCase(get()) }
    viewModel { TopicListViewModel(get(), get()) }
}
