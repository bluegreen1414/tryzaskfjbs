package com.kapirti.social_chat_food_video.ui.presentation.home.explorephoto

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import com.kapirti.social_chat_food_video.common.stateInUi
import com.kapirti.social_chat_food_video.core.room.WatchedExplorePhoto
import com.kapirti.social_chat_food_video.core.room.WatchedExplorePhotoDao
import com.kapirti.social_chat_food_video.model.ExplorePhotoVideo
import com.kapirti.social_chat_food_video.model.service.AccountService
import com.kapirti.social_chat_food_video.model.service.FirestoreService
import com.kapirti.social_chat_food_video.model.service.LogService
import com.kapirti.social_chat_food_video.ui.presentation.KapirtiViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow

@HiltViewModel
class ExplorePhotoViewModel @Inject constructor(
    @ApplicationContext private val application: Context,
    private val accountService: AccountService,
    private val firestoreService: FirestoreService,
    private val watchedExplorePhotoDao: WatchedExplorePhotoDao,
    logService: LogService,
) : KapirtiViewModel(logService) {
    val myId = accountService.currentUserId

    val media: StateFlow<List<ExplorePhotoVideo>> = firestoreService.explorePhotos
        .combine(flow { emit(watchedExplorePhotoDao.getWatchedExploreIds()) }) { videos, watchedIds ->
            videos.filter { it.id !in watchedIds }
        }
        .stateInUi(emptyList())


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
            watchedExplorePhotoDao.insert(WatchedExplorePhoto(id))
        }
    }
}
