package com.kapirti.social_chat_food_video.ui.presentation.player

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.kapirti.social_chat_food_video.model.service.LogService
import com.kapirti.social_chat_food_video.ui.presentation.KapirtiViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class VideoPlayerScreenViewModel @Inject constructor(
    logService: LogService
): KapirtiViewModel(logService) {

    private val _player = MutableStateFlow<Player?>(null)
    val player = _player.asStateFlow()
    var shouldEnterPipMode by mutableStateOf(false)

    fun initializePlayer(uri: String, context: Context) {
        val player = ExoPlayer.Builder(context.applicationContext)
            .build().apply {
                setMediaItem(MediaItem.fromUri(uri))
                prepare()
                playWhenReady = true
            }

        player.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                shouldEnterPipMode = isPlaying
            }
        })

        _player.value = player
        shouldEnterPipMode = true
    }

    fun releasePlayer() {
        _player.value?.release()
        _player.value = null
        shouldEnterPipMode = false
    }
}
