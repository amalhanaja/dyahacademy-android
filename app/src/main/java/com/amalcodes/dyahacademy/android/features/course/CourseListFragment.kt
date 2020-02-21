package com.amalcodes.dyahacademy.android.features.course

import android.graphics.Rect
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.amalcodes.dyahacademy.android.R
import com.amalcodes.dyahacademy.android.core.ItemOffsetDecoration
import com.amalcodes.dyahacademy.android.core.MultiAdapter
import kotlinx.android.synthetic.main.component_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_course_list.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber

class CourseListFragment : Fragment(R.layout.fragment_course_list) {

    private val viewModel: CourseListViewModel by viewModels(
        ownerProducer = { this },
        factoryProducer = {
            CourseListViewModel.Factory
        }
    )

    private val adapter by lazy {
        MultiAdapter()
    }

    @ExperimentalCoroutinesApi
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupView()
        viewModel.uiState.observe(viewLifecycleOwner) {
            when (it) {
                is CourseListUIState.Initial -> onInitialState()
                is CourseListUIState.HasData -> onHasDataState(it.data)
                is CourseListUIState.Error -> onErrorState(it.throwable)
            }
        }
    }

    private fun setupView() {
        toolbar_course_list?.mtv_toolbar_title?.text = getString(R.string.app_name)
        rv_course_list?.adapter = adapter
        rv_course_list?.addItemDecoration(ItemOffsetDecoration { viewHolder, count ->
            val position = viewHolder.adapterPosition
            if (viewHolder is CourseViewHolder) {
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
        })
        adapter.setOnViewHolderClickListener { view, item ->
            when (view.id) {
                R.id.cl_item_course_wrapper -> {
                    require(item is CourseViewEntity)
                    val direction = CourseListFragmentDirections
                        .actionCourseListFragmentToCourseDetailFragment(
                            item.id,
                            item.title,
                            item.createdBy
                        )
                    findNavController().navigate(direction)
                }
            }
        }
    }

    private fun onErrorState(throwable: Throwable) {
        Timber.e(throwable)
    }

    private fun onHasDataState(data: List<CourseViewEntity>) {
        Timber.d(data.joinToString { it.title })
        adapter.submitList(data)
    }

    @ExperimentalCoroutinesApi
    private fun onInitialState() {
        viewModel.fetch()

    }

}
