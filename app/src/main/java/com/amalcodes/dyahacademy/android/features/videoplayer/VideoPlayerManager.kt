package com.amalcodes.dyahacademy.android.features.videoplayer

import android.content.Context
import android.net.Uri
import com.amalcodes.dyahacademy.android.R
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.MergingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util

/**
 * @author: AMAL
 * Created On : 2020-02-15
 */


class VideoPlayerManager(
    context: Context
) {

    private val dataSourceFactory: DataSource.Factory by lazy {
        DefaultDataSourceFactory(
            context, Util.getUserAgent(
                context, context.getString(
                    R.string.app_name
                )
            )
        )
    }

    val player: ExoPlayer by lazy {
        SimpleExoPlayer.Builder(context).build()
    }

    private fun getMediaSource(uri: Uri): MediaSource {
        return ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(uri)
    }

    fun addEventListener(listener: Player.EventListener) {
        player.addListener(listener)
    }

    fun playStream(vararg urls: String) {
        val sources = urls.map {
            val uri = Uri.parse(it)
            getMediaSource(uri)
        }.toTypedArray()
        player.prepare(MergingMediaSource(*sources))
        player.playWhenReady = true
    }

    fun destroyPlayer() {
        player.stop(true)
    }

    companion object {

        @Volatile
        private var instance: VideoPlayerManager? = null

        fun getInstance(context: Context): VideoPlayerManager {
            return instance ?: VideoPlayerManager(context).also {
                instance = it
            }
        }
    }

}