package com.amalcodes.dyahacademy.android.features.course

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.amalcodes.dyahacademy.android.R
import com.amalcodes.dyahacademy.android.core.Failure
import com.amalcodes.dyahacademy.android.core.ItemOffsetDecoration
import com.amalcodes.dyahacademy.android.core.MultiAdapter
import com.amalcodes.dyahacademy.android.core.autoCleared
import com.amalcodes.dyahacademy.android.databinding.FragmentCourseListBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber

class CourseListFragment : Fragment() {

    private val viewModel: CourseListViewModel by viewModels(
        ownerProducer = { this },
        factoryProducer = { CourseListViewModel.Factory }
    )

    private val adapter by autoCleared { MultiAdapter() }
    private var binding: FragmentCourseListBinding by autoCleared()

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
            binding.pbCourseList.isVisible = it is CourseListUIState.Loading
            binding.globalMessage.root.isVisible = it is CourseListUIState.Error
            when (it) {
                is CourseListUIState.Initial -> onInitialState()
                is CourseListUIState.HasData -> onHasDataState(it.data)
                is CourseListUIState.Error -> onErrorState(it.failure)
            }
        }
    }

    private fun setupView() {
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

    @ExperimentalCoroutinesApi
    private fun onErrorState(failure: Failure) {
        binding.globalMessage.btnGlobalMessage.isVisible = true
        binding.globalMessage.btnGlobalMessage.setOnClickListener { viewModel.fetch() }
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

    private fun onHasDataState(data: List<CourseViewEntity>) {
        Timber.d(data.joinToString { it.title })
        adapter.submitList(data)
    }

    @ExperimentalCoroutinesApi
    private fun onInitialState() {
        viewModel.fetch()

    }

}
