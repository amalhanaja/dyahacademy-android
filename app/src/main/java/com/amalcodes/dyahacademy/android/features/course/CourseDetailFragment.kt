package com.amalcodes.dyahacademy.android.features.course

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.amalcodes.dyahacademy.android.R
import com.amalcodes.dyahacademy.android.core.ItemOffsetDecoration
import com.amalcodes.dyahacademy.android.core.MultiAdapter
import com.amalcodes.dyahacademy.android.features.lesson.LessonViewEntity
import com.amalcodes.dyahacademy.android.features.topic.TopicViewHolder
import kotlinx.android.synthetic.main.fragment_course_detail.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber

class CourseDetailFragment : Fragment() {

    private val viewModel: CourseDetailViewModel by viewModels(
        ownerProducer = { this },
        factoryProducer = {
            CourseDetailViewModel.Factory
        }
    )

    private val adapter by lazy {
        MultiAdapter()
    }

    private val args: CourseDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_course_detail, container, false)
    }

    @ExperimentalCoroutinesApi
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupView()
        viewModel.uiState.observe(viewLifecycleOwner) {
            when (it) {
                is CourseDetailUIState.Initial -> onInitialState()
                is CourseDetailUIState.Error -> onErrorState(it.throwable)
                is CourseDetailUIState.HasData -> onHasDataState(it.data)
            }
        }
    }

    private fun setupView() {
        setHasOptionsMenu(true)
        rv_course_detail?.adapter = adapter
        rv_course_detail?.addItemDecoration(
            ItemOffsetDecoration { viewHolder, count ->
                val position = viewHolder.adapterPosition
                if (viewHolder is TopicViewHolder) {
                    return@ItemOffsetDecoration Rect().apply {
                        left = resources.getDimensionPixelSize(R.dimen.spacing_4)
                        right = resources.getDimensionPixelSize(R.dimen.spacing_4)
                        top = when (position) {
                            0 -> resources.getDimensionPixelSize(R.dimen.spacing_6)
                            else -> resources.getDimensionPixelSize(R.dimen.spacing_1)
                        }
                        bottom = when (position) {
                            count - 1 -> resources.getDimensionPixelSize(R.dimen.spacing_6)
                            else -> resources.getDimensionPixelSize(R.dimen.spacing_1)
                        }
                    }
                }
                return@ItemOffsetDecoration Rect()
            }
        )
    }

    private fun onHasDataState(data: CourseDetailViewEntity) {
        Timber.d(data.toString())
        adapter.submitList(data.topics)
        adapter.setOnViewHolderClickListener { view, item ->
            when (view.id) {
                R.id.cl_item_lesson_default_wrapper -> {
                    require(item is LessonViewEntity)
                    val directions = when (item) {
                        is LessonViewEntity.Youtube ->
                            CourseDetailFragmentDirections
                                .actionCourseDetailFragmentToYoutubeLessonFragment(
                                    item.youtubeUrl,
                                    item.title
                                )
                        is LessonViewEntity.Markdown ->
                            CourseDetailFragmentDirections.actionCourseDetailFragmentToMarkdownLessonFragment(
                                item.title,
                                item.content
                            )
                        is LessonViewEntity.Quiz ->
                            CourseDetailFragmentDirections
                                .actionCourseDetailFragmentToQuizFragment(
                                    label = item.title,
                                    lessonId = item.id
                                )
                        else -> throw IllegalStateException("Unsupported Lesson: $item")
                    }
                    findNavController().navigate(directions)
                }
            }
        }
    }

    private fun onErrorState(throwable: Throwable) {
        Timber.e(throwable)
    }

    @ExperimentalCoroutinesApi
    private fun onInitialState() {
        viewModel.fetch(args.courseId)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> findNavController().navigateUp()
            else -> super.onOptionsItemSelected(item)
        }
    }


}
