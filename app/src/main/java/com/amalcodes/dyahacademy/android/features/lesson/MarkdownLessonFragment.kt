package com.amalcodes.dyahacademy.android.features.lesson

import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.amalcodes.dyahacademy.android.R

/**
 * @author: AMAL
 * Created On : 2020-02-16
 */


class MarkdownLessonFragment : Fragment(R.layout.fragment_markdown_lesson) {

//    private val args: MarkdownLessonFragmentArgs by navArgs()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)
//        Injector.markwon.setMarkdown(actv_markdown_lesson, args.markdown)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> findNavController().navigateUp()
            else -> super.onOptionsItemSelected(item)
        }
    }

}