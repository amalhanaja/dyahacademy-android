package com.amalcodes.dyahacademy.android.features.lesson

import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import at.huber.youtubeExtractor.VideoMeta
import at.huber.youtubeExtractor.YouTubeExtractor
import at.huber.youtubeExtractor.YtFile
import com.amalcodes.dyahacademy.android.R
import com.amalcodes.dyahacademy.android.core.autoCleared
import com.amalcodes.dyahacademy.android.core.manager.VideoPlayerManager
import com.amalcodes.dyahacademy.android.databinding.FragmentYoutubeLessonBinding
import com.google.android.exoplayer2.Player
import kotlinx.android.synthetic.main.exo_controller.*
import timber.log.Timber


class YoutubeLessonFragment : Fragment() {

    private val videoPlayerManager: VideoPlayerManager by lazy {
        VideoPlayerManager.getInstance(requireContext())
    }

    private val args: YoutubeLessonFragmentArgs by navArgs()

    private var binding: FragmentYoutubeLessonBinding by autoCleared()

    private var startPosition = 0L

    private val videoListener = object : Player.EventListener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            super.onIsPlayingChanged(isPlaying)
            if (!isStartPositionSeeked) {
                videoPlayerManager.player.seekTo(startPosition)
                isStartPositionSeeked = true
            }
        }

        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            when (playbackState) {
                Player.STATE_BUFFERING -> binding.videoView.hideController()
                else -> {
                }
            }
        }
    }

    private var isStartPositionSeeked = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentYoutubeLessonBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        startPosition = savedInstanceState?.getLong("POSITION") ?: 0L
        videoPlayerManager.addEventListener(videoListener)
        setupView()
        extractYoutubeUrl(args.youtubeUrl)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        videoPlayerManager.removeEventListener(videoListener)
    }

    private fun setupView() {
        binding.actvYoutubeLessonTitle.text = args.label
        iv_back?.setOnClickListener {
            onBackPress()
        }
        requireActivity().onBackPressedDispatcher.addCallback(this) { onBackPress() }
        when (requireActivity().requestedOrientation) {
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE -> enterFullScreen()
            else -> exitFullScreen()
        }
        btn_full?.setOnClickListener {
            changeScreenOrientation()
        }
        binding.videoView.player = videoPlayerManager.player
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

    private fun onBackPress() {
        when (requireActivity().requestedOrientation) {
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE -> changeScreenOrientation()
            else -> findNavController().navigateUp()
        }
    }

    private fun exitFullScreen() {
        val params = binding.videoView.layoutParams as ConstraintLayout.LayoutParams
        params.width = ConstraintLayout.LayoutParams.MATCH_PARENT
        params.height = resources.getDimensionPixelSize(R.dimen.video_size)
        binding.videoView.layoutParams = params
        btn_full?.setImageResource(R.drawable.ic_maximize)
    }

    private fun enterFullScreen() {
        val params = binding.videoView.layoutParams as ConstraintLayout.LayoutParams
        params.width = ConstraintLayout.LayoutParams.MATCH_PARENT
        params.height = ConstraintLayout.LayoutParams.MATCH_PARENT
        binding.videoView.layoutParams = params
        btn_full?.setImageResource(R.drawable.ic_minimize)
        hideSystemUI()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong("POSITION", videoPlayerManager.player.currentPosition)
    }

    private fun hideSystemUI() {
        binding.videoView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }

    private fun extractYoutubeUrl(url: String) {
        YoutubeExtractor(requireContext()) { ytFiles, videoMeta ->
            if (ytFiles != null) {
                println(ytFiles)
                ytFiles.get(22)?.let { ytFile720 ->
                    videoPlayerManager.playStream(ytFile720.url)
                } ?: ytFiles.get(135)?.let { ytFile480 ->
                    videoPlayerManager.playStream(ytFile480.url, ytFiles[140].url)
                }
            }
            Timber.d(ytFiles.toString())
        }.extract(url, true, true)
    }

    override fun onStart() {
        super.onStart()
        videoPlayerManager.player.playWhenReady = true
    }

    override fun onStop() {
        super.onStop()
        videoPlayerManager.player.playWhenReady = false

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
