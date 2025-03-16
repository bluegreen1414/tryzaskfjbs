package com.kapirti.social_chat_food_video.ui.presentation.home.explore

import android.content.Context
import android.net.Uri
import android.os.HandlerThread
import android.os.Process
import android.util.Log
import androidx.annotation.OptIn
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.VideoSize
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.ExoPlayer
import com.kapirti.social_chat_food_video.common.stateInUi
import com.kapirti.social_chat_food_video.core.datastore.CameraTypeRepository
import com.kapirti.social_chat_food_video.core.room.WatchedExplore
import com.kapirti.social_chat_food_video.core.room.WatchedExploreDao
import com.kapirti.social_chat_food_video.model.ExplorePhotoVideo
import com.kapirti.social_chat_food_video.model.service.AccountService
import com.kapirti.social_chat_food_video.model.service.FirestoreService
import com.kapirti.social_chat_food_video.model.service.LogService
import com.kapirti.social_chat_food_video.model.service.impl.FirestoreServiceImpl
import com.kapirti.social_chat_food_video.ui.presentation.KapirtiViewModel
import com.kapirti.social_chat_food_video.ui.presentation.player.preloadmanager.PreloadManagerWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow

@UnstableApi
@HiltViewModel
class ExploreViewModel @Inject constructor(
    @ApplicationContext private val application: Context,
    private val accountService: AccountService,
    private val firestoreService: FirestoreService,
    private val cameraTypeRepository: CameraTypeRepository,
    private val watchedExploreDao: WatchedExploreDao,
    logService: LogService,
) : KapirtiViewModel(logService) {
    val myId = accountService.currentUserId
    // List of videos and photos from chats
   // var media by mutableStateOf<List<ExploreMediaItem>>(emptyList())

    // Single player instance - in the future, we can implement a pool of players to improve
    // latency and allow for concurrent playback
    var player by mutableStateOf<ExoPlayer?>(null)

    // Width/Height ratio of the current media item, used to properly size the Surface
    var videoRatio by mutableStateOf<Float?>(null)

    // Preload Manager for preloaded multiple videos
    private val enablePreloadManager: Boolean = true
    private lateinit var preloadManager: PreloadManagerWrapper

    // Playback thread; Internal playback / preload operations are running on the playback thread.
    private val playerThread: HandlerThread =
        HandlerThread("playback-thread", Process.THREAD_PRIORITY_AUDIO)

    var playbackStartTimeMs = C.TIME_UNSET

    private val videoSizeListener = object : Player.Listener {
        override fun onVideoSizeChanged(videoSize: VideoSize) {
            videoRatio = if (videoSize.height > 0 && videoSize.width > 0) {
                videoSize.width.toFloat() / videoSize.height.toFloat()
            } else {
                null
            }
            super.onVideoSizeChanged(videoSize)
        }
    }

    private val firstFrameListener = object : Player.Listener {
        override fun onRenderedFirstFrame() {
            val timeToFirstFrameMs = System.currentTimeMillis() - playbackStartTimeMs
            Log.d("PreloadManager", "\t\tTime to first Frame = $timeToFirstFrameMs ")
            super.onRenderedFirstFrame()
        }
    }

    val media: StateFlow<List<ExplorePhotoVideo>> = firestoreService.explores
        .combine(flow { emit(watchedExploreDao.getWatchedExploreIds()) }) { videos, watchedIds ->
            videos.filter { it.id !in watchedIds }
        }
        .stateInUi(emptyList())



/**

    init {
        launchCatching {
            val newList = mutableListOf<ExploreMediaItem>()
            ff.collection(FirestoreServiceImpl.EXPLORE_COLLECTION).get().addOnCompleteListener {
                if (it.isSuccessful && it.result != null) {
                    val x = it.result.toObjects(ExploreMediaItem::class.java)

                    for (i in x){
                        newList.add(i)
                    }
                    newList.sortByDescending { it.dateOfCreation }
                    media = newList

                }
            }
        }
    }


  val explores = firestoreService.explores()
            if(explores != null){

            }

            val allChats = repository.getChats().first()
            val newList = mutableListOf<ExploreMediaItem>()
            for (chatDetail in allChats) {
                val messages = repository.findMessages(chatDetail.chatWithLastMessage.id).first()
                for (message in messages) {
                    if (message.mediaUri != null) {
                        newList += ExploreMediaItem(
                            uri = message.mediaUri,
                            type = if (message.mediaMimeType?.contains("video") == true) {
                                ExploreMediaType.VIDEO
                            } else {
                                ExploreMediaType.PHOTO
                            },
                            timestamp = message.timestamp,
                            chatName = chatDetail.firstContact.name,
                            chatIconUri = chatDetail.firstContact.iconUri,
                        )
                    }
                }
            }
            newList.sortByDescending { it.timestamp }
            media = newList*/


    @OptIn(UnstableApi::class) // https://developer.android.com/guide/topics/media/media3/getting-started/migration-guide#unstableapi
    fun initializePlayer(restartAppExplore: () -> Unit) {
        try {
            if (player != null) return

            // Reduced buffer durations since the primary use-case is for short-form videos
            val loadControl =
                DefaultLoadControl.Builder()
                    .setBufferDurationsMs(
                        5_000,
                        20_000,
                        5_00,
                        DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_MS
                    )
                    .setPrioritizeTimeOverSizeThresholds(true).build()

            playerThread.start()


            val newPlayer = ExoPlayer
                .Builder(application.applicationContext)
                .setLoadControl(loadControl)
                .setPlaybackLooper(playerThread.looper)
                .build()
                .also {
                    it.repeatMode = ExoPlayer.REPEAT_MODE_ONE
                    it.playWhenReady = true
                    it.addListener(videoSizeListener)
                    it.addListener(firstFrameListener)
                }

            videoRatio = null
            player = newPlayer

            if (enablePreloadManager) {
                initPreloadManager(loadControl, playerThread)
            }
        } catch (e: Exception) {
            restartAppExplore()
        }
    }



    private fun initPreloadManager(
        loadControl: DefaultLoadControl,
        preloadAndPlaybackThread: HandlerThread,
    ) {
        preloadManager =
            PreloadManagerWrapper.build(
                preloadAndPlaybackThread.looper,
                loadControl,
                application.applicationContext,
            )
        preloadManager.setPreloadWindowSize(5)

        // Add videos to preload
        if (media.value.isNotEmpty()) {
            preloadManager.init(media.value)
        }
    }

    fun releasePlayer() {
        if (enablePreloadManager) {
            preloadManager.release()
        }
        player?.apply {
            removeListener(videoSizeListener)
            removeListener(firstFrameListener)
            release()
        }
        playerThread.quit()
        videoRatio = null
        player = null
    }

    fun changePlayerItem(uri: Uri?, currentPlayingIndex: Int) {
        if (player == null) return

        player?.apply {
            stop()
            videoRatio = null
            if (uri != null) {
                // Set the right source to play
                val mediaItem = MediaItem.fromUri(uri)

                if (enablePreloadManager) {
                    val mediaSource = preloadManager.getMediaSource(mediaItem)
                    Log.d("PreloadManager", "Mediasource $mediaSource ")

                    if (mediaSource == null) {
                        setMediaItem(mediaItem)
                    } else {
                        // Use the preloaded media source
                        setMediaSource(mediaSource)
                    }
                    preloadManager.setCurrentPlayingIndex(currentPlayingIndex)
                } else {
                    setMediaItem(mediaItem)
                }

                playbackStartTimeMs = System.currentTimeMillis()
                Log.d("PreloadManager", "Video Playing $uri ")
                prepare()
            } else {
                clearMediaItems()
            }
        }
    }


    var showReportDialog = mutableStateOf(false)
    var showReportDone = mutableStateOf(false)
    private val timeline = mutableStateOf<ExplorePhotoVideo?>(null)
    fun onReportClick(newValue: ExplorePhotoVideo) {
        showReportDialog.value = true
        timeline.value = newValue
    }
    fun onReportButtonClick() {
        launchCatching {
            //firestoreService.saveReportTimeline(timeline.value ?: Timeline())
            showReportDialog.value = false
            showReportDone.value = true
        }
    }
    fun onReportDoneDismiss() {
        launchCatching {
            showReportDone.value = false
        }
    }

    fun markAsWatched(id: String) {
        launchCatching {
            watchedExploreDao.insert(WatchedExplore(id))
        }
    }

    fun onAddClick(onAddClick: () -> Unit){
        launchCatching {
            cameraTypeRepository.saveState(FirestoreServiceImpl.EXPLORE_COLLECTION)
            onAddClick()
        }
    }
}
