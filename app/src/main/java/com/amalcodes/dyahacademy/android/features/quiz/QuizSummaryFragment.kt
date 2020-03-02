package com.amalcodes.dyahacademy.android.features.quiz


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.amalcodes.dyahacademy.android.R
import com.amalcodes.dyahacademy.android.core.autoCleared
import com.amalcodes.dyahacademy.android.databinding.FragmentQuizSummaryBinding
import kotlinx.android.synthetic.main.fragment_quiz_summary.*

/**
 * A simple [Fragment] subclass.
 */
class QuizSummaryFragment : Fragment() {

    private val args: QuizSummaryFragmentArgs by navArgs()

    private var binding: FragmentQuizSummaryBinding by autoCleared()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = FragmentQuizSummaryBinding.inflate(inflater, container, false)
        .also { binding = it }
        .root

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupView()
    }

    private fun setupView() {
        binding.mtvQuizSummaryDescription.text =
            getString(R.string.text_quizSummaryDescription, args.lessonTitle)
        binding.mtvQuizSummaryScore.text = args.quizSummary.score.toString()
        binding.mtvQuizSummaryBlankAnswer.text = getString(
            R.string.text_blankAnswerCount,
            args.quizSummary.blankAnswer
        )
        binding.mtvQuizSummaryCorrectAnswer.text = getString(
            R.string.text_correctAnswerCount,
            args.quizSummary.correctAnswer
        )
        binding.mtvQuizSummaryWrongAnswer.text = getString(
            R.string.text_correctWrongCount,
            args.quizSummary.wrongAnswer
        )
        binding.mbQuizSummaryContinueLearning.setOnClickListener {
            findNavController().navigateUp()
        }
        mb_quiz_summary_continue_learning.setOnClickListener {
            val direction = QuizSummaryFragmentDirections.actionQuizSummaryFragmentToQuizFragment(
                label = args.lessonTitle,
                lessonId = args.lessonId,
                answers = args.answers
            )
            findNavController().navigate(direction)
        }
    }

}
