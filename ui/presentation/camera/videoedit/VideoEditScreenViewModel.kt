package com.kapirti.social_chat_food_video.ui.presentation.camera.videoedit

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.media3.common.util.UnstableApi
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.kapirti.social_chat_food_video.core.constants.SelectType
import com.kapirti.social_chat_food_video.core.datastore.CameraTypeRepository
import com.kapirti.social_chat_food_video.model.service.AccountService
import com.kapirti.social_chat_food_video.model.service.FirestoreService
import com.kapirti.social_chat_food_video.model.service.LogService
import com.kapirti.social_chat_food_video.model.service.impl.FirestoreServiceImpl
import com.kapirti.social_chat_food_video.ui.presentation.KapirtiViewModel
import com.kapirti.social_chat_food_video.ui.presentation.camera.func.VideoChatWorker
import com.kapirti.social_chat_food_video.ui.presentation.camera.func.VideoExploreWorker
import com.kapirti.social_chat_food_video.ui.presentation.camera.func.VideoStoryWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.UUID
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow

@UnstableApi
@HiltViewModel
class VideoEditScreenViewModel @Inject constructor(
    @ApplicationContext private val application: Context,
    private val cameraTypeRepository: CameraTypeRepository,
    private val accountService: AccountService,
    private val firestoreService: FirestoreService,
    logService: LogService
    //private val repository: ChatRepository,
) : KapirtiViewModel(logService) {
    private val chatId = MutableStateFlow("0L")
    private val _cameraType = mutableStateOf<String?>(null)
    private val myId = accountService.currentUserId
    private val _username = mutableStateOf<String?>(null)
    private val _photo = mutableStateOf<String?>(null)
    val doneBtnWorking = MutableStateFlow(false)

    init {
        launchCatching {
            cameraTypeRepository.readState().collect {
                _cameraType.value = it
                firestoreService.getUser(accountService.currentUserId)?.let {
                    when(it.type){
                        SelectType.FLIRT -> {
                            firestoreService.getUserFlirt(it.country, accountService.currentUserId)?.let {
                                _username.value = it.username
                                _photo.value = it.photo
                            }
                        }
                        SelectType.MARRIAGE -> {
                            firestoreService.getUserMarriage(it.country, accountService.currentUserId)?.let {
                                _username.value = it.username
                                _photo.value = it.photo
                            }
                        }
                        SelectType.LANGUAGE_PRACTICE -> {
                            firestoreService.getUserLP(it.country, accountService.currentUserId)?.let {
                                _username.value = it.username
                                _photo.value = it.photo
                            }
                        }
                        SelectType.HOTEL -> {
                            firestoreService.getHotel(it.country, accountService.currentUserId)?.let {
                                _username.value = it.username
                                _photo.value = it.photo
                            }
                        }
                        SelectType.RESTAURANT -> {
                            firestoreService.getRestaurant(it.country, accountService.currentUserId)?.let {
                                _username.value = it.username
                                _photo.value = it.photo
                            }
                        }
                        SelectType.CAFE -> {
                            firestoreService.getCafe(it.country, accountService.currentUserId)?.let {
                                _username.value = it.username
                                _photo.value = it.photo
                            }
                        }
                        SelectType.NOPE -> {}
                        else -> {}
                    }
                }
            }
        }
    }
    fun setChatId(chatId: String) {
        this.chatId.value = chatId
    }

    @androidx.annotation.OptIn(UnstableApi::class)
    fun applyVideoTransformation(
        videoUri: String,
        restartAppChat: () -> Unit,
        restartAppExplore: () -> Unit,
        restartAppStory: () -> Unit
    ) {
        launchCatching {
            doneBtnWorking.value = true
            val randomUid = UUID.randomUUID().toString()

            if (_cameraType.value == FirestoreServiceImpl.EXPLORE_COLLECTION) {
                saveExploreVideo(randomUid, videoUri, restartAppExplore)
            } else if (_cameraType.value == FirestoreServiceImpl.STORY_COLLECTION) {
                saveStoryVideo(randomUid, videoUri, restartAppStory)
            } else if (_cameraType.value == FirestoreServiceImpl.CHAT_COLLECTION){
                saveChatVideo(randomUid, videoUri, restartAppChat)
            } else return@launchCatching
        }
    }
    private fun saveExploreVideo(randomUid: String, uri: String, popUp: () -> Unit) {
        val inputData = workDataOf(
            FirestoreServiceImpl.CHAT_COLLECTION to randomUid.toString(),
            FirestoreServiceImpl.CAFE_COLLECTION to myId.toString(),
            FirestoreServiceImpl.EXPLORE_COLLECTION to uri.toString(),
            FirestoreServiceImpl.USERNAME_FIELD to _username.value.toString(),
            FirestoreServiceImpl.PHOTO_FIELD to _photo.value.toString(),
            FirestoreServiceImpl.DESCRIPTION_FIELD to "video",
        )

        val uploadRequest = OneTimeWorkRequestBuilder<VideoExploreWorker>()
            .setInputData(inputData)
            .build()

        WorkManager.getInstance(application).enqueue(uploadRequest)
        popUp()
    }
    private fun saveStoryVideo(randomUid: String, uri: String, popUp: () -> Unit) {
        val inputData = workDataOf(
            FirestoreServiceImpl.CHAT_COLLECTION to randomUid.toString(),
            FirestoreServiceImpl.CAFE_COLLECTION to myId.toString(),
            FirestoreServiceImpl.EXPLORE_COLLECTION to uri.toString(),
            FirestoreServiceImpl.USERNAME_FIELD to _username.value.toString(),
            FirestoreServiceImpl.PHOTO_FIELD to _photo.value.toString(),
            FirestoreServiceImpl.DESCRIPTION_FIELD to "video",
        )

        val uploadRequest = OneTimeWorkRequestBuilder<VideoStoryWorker>()
            .setInputData(inputData)
            .build()

        WorkManager.getInstance(application).enqueue(uploadRequest)
        popUp()
    }
    private fun saveChatVideo(randomUid: String, uri: String, popUp: () -> Unit) {
        val inputData = workDataOf(
            FirestoreServiceImpl.CHAT_COLLECTION to randomUid.toString(),
            FirestoreServiceImpl.CAFE_COLLECTION to myId.toString(),
            FirestoreServiceImpl.EXPLORE_COLLECTION to uri.toString(),
            FirestoreServiceImpl.USERNAME_FIELD to chatId.value.toString(),
        )

        val uploadRequest = OneTimeWorkRequestBuilder<VideoChatWorker>()
            .setInputData(inputData)
            .build()

        WorkManager.getInstance(application).enqueue(uploadRequest)
        popUp()
    }
}
