package com.amalcodes.dyahacademy.android.features.lesson

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.amalcodes.dyahacademy.android.R
import com.amalcodes.dyahacademy.android.core.Injector
import kotlinx.android.synthetic.main.fragment_markdown_lesson.*

/**
 * @author: AMAL
 * Created On : 2020-02-16
 */


class MarkdownLessonFragment : Fragment(R.layout.fragment_markdown_lesson) {

    private val args: MarkdownLessonFragmentArgs by navArgs()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Injector.markwon.setMarkdown(actv_markdown_lesson, args.markdown)
    }
}