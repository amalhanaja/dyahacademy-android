package com.amalcodes.dyahacademy.android.features.topic

import android.graphics.Rect
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.amalcodes.dyahacademy.android.R
import com.amalcodes.dyahacademy.android.core.ItemOffsetDecoration
import com.amalcodes.dyahacademy.android.core.MultiAdapter
import com.amalcodes.dyahacademy.android.features.lesson.LessonGroupTitleViewHolder
import com.amalcodes.dyahacademy.android.features.lesson.LessonType
import com.amalcodes.dyahacademy.android.features.lesson.LessonViewEntity
import com.amalcodes.dyahacademy.android.features.lesson.LessonViewHolder
import com.amalcodes.ezrecyclerview.adapter.entity.ItemEntity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.component_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_topic_detail.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber

class TopicDetailFragment : Fragment(R.layout.fragment_topic_detail) {

    private val viewModel: TopicDetailViewModel by viewModels(
        ownerProducer = { this },
        factoryProducer = { TopicDetailViewModel.Factory }
    )

    private val args: TopicDetailFragmentArgs by navArgs()

    private val adapter by lazy {
        MultiAdapter()
    }

    @ExperimentalCoroutinesApi
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupView()
        viewModel.uiState.observe(viewLifecycleOwner) {
            pb?.isVisible = it is TopicDetailUIState.Loading
            when (it) {
                is TopicDetailUIState.Initial -> onInitialState()
                is TopicDetailUIState.Error -> onErrorState(it.throwable)
                is TopicDetailUIState.HasData -> onHasDataState(it.data)
            }
        }
    }

    private fun setupView() {
        toolbar_topic_detail?.mtv_toolbar_title?.text = args.label
        toolbar_topic_detail?.iv_back?.setOnClickListener {
            findNavController().navigateUp()
        }
        toolbar_topic_detail?.iv_back?.isVisible = true
        rv_topic_detail?.adapter = adapter
        rv_topic_detail?.addItemDecoration(
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
                    val direction = when (item.type) {
                        LessonType.YOUTUBE -> TopicDetailFragmentDirections
                            .actionTopicDetailFragmentToYoutubeLessonFragment(
                                youtubeUrl = requireNotNull(item.youtubeUrl),
                                label = item.title
                            )
                        LessonType.QUIZ -> TopicDetailFragmentDirections
                            .actionTopicDetailFragmentToQuizFragment(
                                label = item.title,
                                lessonId = item.id
                            )
                        else -> throw IllegalStateException("unexpected LessonType: ${item.type}")
                    }
                    findNavController().navigate(direction)
                }
            }
        }
    }

    @ExperimentalCoroutinesApi
    private fun onInitialState() {
        viewModel.fetch(args.topicId)
    }

    @ExperimentalCoroutinesApi
    private fun onErrorState(throwable: Throwable) {
        Timber.e(throwable)
        Snackbar.make(parent, R.string.text_error_general, Snackbar.LENGTH_LONG)
            .setAction(R.string.text_Try_Again) { viewModel.fetch(args.topicId) }
            .show()
    }

    private fun onHasDataState(data: List<ItemEntity>) {
        adapter.submitList(data)
    }

}
