package com.amalcodes.dyahacademy.android.features.quiz

import android.graphics.Rect
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.navArgs
import com.amalcodes.dyahacademy.android.R
import com.amalcodes.dyahacademy.android.core.Injector
import com.amalcodes.dyahacademy.android.core.ItemOffsetDecoration
import com.amalcodes.dyahacademy.android.core.MultiAdapter
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


    private val adapter by lazy {
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
                is QuizUIState.HasData -> onHasDataState(it.data)
            }
        }
    }

    private fun onHasDataState(data: QuizViewEntity) {
        Injector.markwon.setMarkdown(actv_question, data.question)
        adapter.submitList(data.answers)
        adapter.setOnViewHolderClickListener { view, item ->
            when (view.id) {
                R.id.mrb_item_answer -> {
                    require(item is AnswerViewEntity)
                    val newAnswer = data.answers.map {
                        it.copy(isSelected = item == it)
                    }
                    btn_check?.isEnabled = newAnswer.any { it.isSelected }
                    btn_check?.setOnClickListener {
                        if (item.isCorrect) {
                            Toast.makeText(requireContext(), "BENAR", Toast.LENGTH_SHORT).show()
                            viewModel.next()
                        } else {
                            Toast.makeText(requireContext(), "TET TOOOTT", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                    adapter.submitList(newAnswer)
                }
            }
        }
        tv_current_per_count?.text = "${data.current}/${data.count}"
    }

    private fun onErrorState(throwable: Throwable) {
        Timber.e(throwable)
    }

    @ExperimentalCoroutinesApi
    private fun onInitialState() {
        viewModel.fetch(args.lessonId)
    }

    private fun setupView() {
        rv_answer?.adapter = adapter
        rv_answer?.addItemDecoration(ItemOffsetDecoration { viewHolder, count ->
            val position = viewHolder.adapterPosition
            if (viewHolder is AnswerViewHolder) {
                return@ItemOffsetDecoration Rect().apply {
                    left = resources.getDimensionPixelSize(R.dimen.spacing_4)
                    right = resources.getDimensionPixelSize(R.dimen.spacing_4)
                    top = when (position) {
                        0 -> resources.getDimensionPixelSize(R.dimen.spacing_4)
                        else -> resources.getDimensionPixelSize(R.dimen.spacing_1)
                    }
                    bottom = when (position) {
                        count - 1 -> resources.getDimensionPixelSize(R.dimen.spacing_4)
                        else -> resources.getDimensionPixelSize(R.dimen.spacing_1)
                    }
                }
            }
            return@ItemOffsetDecoration Rect()
        })
    }

}
