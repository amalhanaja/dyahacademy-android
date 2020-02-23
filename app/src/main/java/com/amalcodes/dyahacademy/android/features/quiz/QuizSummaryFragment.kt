package com.amalcodes.dyahacademy.android.features.quiz


import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.amalcodes.dyahacademy.android.R
import kotlinx.android.synthetic.main.fragment_quiz_summary.*

/**
 * A simple [Fragment] subclass.
 */
class QuizSummaryFragment : Fragment(R.layout.fragment_quiz_summary) {

    private val args: QuizSummaryFragmentArgs by navArgs()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupView()
    }

    private fun setupView() {
        mtv_quiz_summary_description?.text =
            getString(R.string.text_quizSummaryDescription, args.lessonTitle)
        mtv_quiz_summary_score?.text = args.quizSummary.score.toString()
        mtv_quiz_summary_blank_answer?.text = getString(
            R.string.text_blankAnswerCount,
            args.quizSummary.blankAnswer
        )
        mtv_quiz_summary_correct_answer?.text = getString(
            R.string.text_correctAnswerCount,
            args.quizSummary.correctAnswer
        )
        mtv_quiz_summary_wrong_answer?.text = getString(
            R.string.text_correctWrongCount,
            args.quizSummary.wrongAnswer
        )
        mb_quiz_summary_continue_learning?.setOnClickListener {
            findNavController().navigateUp()
        }
        mb_quiz_summary_show_answer?.setOnClickListener {
            val direction = QuizSummaryFragmentDirections.actionQuizSummaryFragmentToQuizFragment(
                label = args.lessonTitle,
                lessonId = args.lessonId,
                answers = args.answers
            )
            findNavController().navigate(direction)
        }
    }

}
