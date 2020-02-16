package com.amalcodes.dyahacademy.android.features.lesson

import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.SparseArray
import android.view.View
import androidx.activity.addCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import at.huber.youtubeExtractor.VideoMeta
import at.huber.youtubeExtractor.YouTubeExtractor
import at.huber.youtubeExtractor.YtFile
import com.amalcodes.dyahacademy.android.R
import com.amalcodes.dyahacademy.android.features.videoplayer.VideoPlayerManager
import com.google.android.exoplayer2.Player
import kotlinx.android.synthetic.main.exo_controller.*
import kotlinx.android.synthetic.main.fragment_youtube_lesson.*
import timber.log.Timber


class YoutubeLessonFragment : Fragment(R.layout.fragment_youtube_lesson) {

    private lateinit var viewModel: YoutubeLessonViewModel

    private val videoPlayerManager: VideoPlayerManager by lazy {
        VideoPlayerManager.getInstance(requireContext())
    }

    private val args: YoutubeLessonFragmentArgs by navArgs()

    private var isStartPositionSeeked = false

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val startPosition = savedInstanceState?.getLong("POSITION") ?: 0L
        videoPlayerManager.addEventListener(object : Player.EventListener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                super.onIsPlayingChanged(isPlaying)
                if (!isStartPositionSeeked) {
                    videoPlayerManager.player.seekTo(startPosition)
                    isStartPositionSeeked = true
                }
            }

            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                pb_exo_controller?.isVisible = playbackState == Player.STATE_BUFFERING
                exo_play?.isGone = true
                exo_pause?.isGone = true
            }
        })
        setupView()
        extractYoutubeUrl(args.youtubeUrl)
    }

    private fun setupView() {
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            when (requireActivity().requestedOrientation) {
                ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE -> changeScreenOrientation()
                else -> findNavController().navigateUp()
            }
        }
        when (requireActivity().requestedOrientation) {
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE -> enterFullScreen()
            else -> exitFullScreen()
        }
        btn_full?.setOnClickListener {
            changeScreenOrientation()
        }
        video_view?.player = videoPlayerManager.player
    }

    private fun changeScreenOrientation() {
        requireActivity().requestedOrientation = when (requireActivity().requestedOrientation) {
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE -> {
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            }
            else -> {
                ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            }
        }
    }

    private fun exitFullScreen() {
        val params = video_view.layoutParams as ConstraintLayout.LayoutParams
        params.width = ConstraintLayout.LayoutParams.MATCH_PARENT
        params.height = (resources.displayMetrics.density * 216 + 0.5f).toInt()
        requireActivity().actionBar?.show()
        video_view.layoutParams = params
    }

    private fun enterFullScreen() {
        val params = video_view.layoutParams as ConstraintLayout.LayoutParams
        params.width = ConstraintLayout.LayoutParams.MATCH_PARENT
        params.height = ConstraintLayout.LayoutParams.MATCH_PARENT
        video_view.layoutParams = params
        requireActivity().actionBar?.hide()
        hideSystemUI()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong("POSITION", videoPlayerManager.player.currentPosition)
    }

    private fun hideSystemUI() {
        video_view.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }

    private fun extractYoutubeUrl(url: String) {
        YoutubeExtractor(requireContext()) { ytFiles, videoMeta ->
            if (ytFiles != null) {
                ytFiles.get(22)?.let { ytFile720 ->
                    videoPlayerManager.playStream(ytFile720.url)
                } ?: ytFiles.get(135)?.let { ytFile480 ->
                    videoPlayerManager.playStream(ytFile480.url, ytFiles[140].url)
                }
            }
            Timber.d(ytFiles.toString())
            Timber.d(videoMeta?.toString())
        }.extract(url, true, true)
    }

    override fun onDestroy() {
        super.onDestroy()
        videoPlayerManager.destroyPlayer()
    }

    private class YoutubeExtractor(
        con: Context,
        private val onExtractionComplete: (ytFiles: SparseArray<YtFile>?, videoMeta: VideoMeta?) -> Unit
    ) : YouTubeExtractor(con) {
        override fun onExtractionComplete(ytFiles: SparseArray<YtFile>?, videoMeta: VideoMeta?) {
            onExtractionComplete.invoke(ytFiles, videoMeta)
        }
    }
}
