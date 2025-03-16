package com.kapirti.social_chat_food_video.ui.presentation.camera.photoedit

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import androidx.compose.runtime.mutableStateOf
import androidx.core.net.toUri
import androidx.media3.common.util.UnstableApi
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.kapirti.social_chat_food_video.common.func.compressPhoto
import com.kapirti.social_chat_food_video.core.constants.SelectType
import com.kapirti.social_chat_food_video.core.datastore.CameraTypeRepository
import com.kapirti.social_chat_food_video.model.service.AccountService
import com.kapirti.social_chat_food_video.model.service.FirestoreService
import com.kapirti.social_chat_food_video.model.service.LogService
import com.kapirti.social_chat_food_video.model.service.StorageService
import com.kapirti.social_chat_food_video.model.service.impl.FirestoreServiceImpl
import com.kapirti.social_chat_food_video.ui.presentation.KapirtiViewModel
import com.kapirti.social_chat_food_video.ui.presentation.camera.func.PhotoChatWorker
import com.kapirti.social_chat_food_video.ui.presentation.camera.func.PhotoExploreWorker
import com.kapirti.social_chat_food_video.ui.presentation.camera.func.PhotoStoryWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.UUID
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow

@UnstableApi
@HiltViewModel
class PhotoEditViewModel @Inject constructor(
    @ApplicationContext private val application: Context,
    private val cameraTypeRepository: CameraTypeRepository,
    private val accountService: AccountService,
    private val firestoreService: FirestoreService,
    private val storageService: StorageService,
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


    private val _bitmap = mutableStateOf<Bitmap?>(null)
    val bitmap
        get() = _bitmap.value

    @androidx.annotation.OptIn(UnstableApi::class)
    fun applyVideoTransformation(
        videoUri: String,
        restartAppChat: () -> Unit,
        restartAppExplore: () -> Unit,
        restartAppStory: () -> Unit
    ) {
        launchCatching {
            doneBtnWorking.value = true
            val uri = videoUri.toUri()

            if (Build.VERSION.SDK_INT < 28) {
                _bitmap.value = MediaStore.Images.Media.getBitmap(application.contentResolver, uri)
                saveImageBody(
                    restartAppChat,
                    restartAppExplore,
                    restartAppStory
                )
            } else {
                val source = ImageDecoder.createSource(application.contentResolver, uri)
                _bitmap.value = ImageDecoder.decodeBitmap(source)
                saveImageBody(
                    restartAppChat,
                    restartAppExplore,
                    restartAppStory
                )
            }
        }
    }
    private fun saveImageBody(
        restartAppChat: () -> Unit,
        restartAppExplore: () -> Unit,
        restartAppStory: () -> Unit
    ) {
        launchCatching {
            _bitmap.value?.let { bitmapNew ->
                val compressedPhoto = compressPhoto(bitmap!!)
                val randomUid = UUID.randomUUID().toString()

                storageService.savePhoto(compressedPhoto, uid = randomUid)
                val link = storageService.getPhoto(randomUid)


                if (_cameraType.value == FirestoreServiceImpl.EXPLORE_COLLECTION){ saveExplorePhoto(link, restartAppExplore) }
                else if(_cameraType.value == FirestoreServiceImpl.STORY_COLLECTION){ saveStoryPhoto(link, restartAppStory) }
                else if (_cameraType.value == FirestoreServiceImpl.CHAT_COLLECTION){ saveChatPhoto(link, restartAppChat) }
                else return@launchCatching
            }
        }
    }
    private fun saveExplorePhoto(link: String, popUp: () -> Unit) {
        val inputData = workDataOf(
            FirestoreServiceImpl.ADDRESS_FIELD to myId,
            FirestoreServiceImpl.EXPLORE_COLLECTION to link,
            FirestoreServiceImpl.USERNAME_FIELD to _username.value,
            FirestoreServiceImpl.PHOTO_FIELD to _photo.value,
            FirestoreServiceImpl.DESCRIPTION_FIELD to "text",
        )

        val uploadRequest = OneTimeWorkRequestBuilder<PhotoExploreWorker>()
            .setInputData(inputData)
            .build()

        WorkManager.getInstance(application).enqueue(uploadRequest)
        popUp()
    }
    private fun saveStoryPhoto(link: String, popUp: () -> Unit) {
        val inputData = workDataOf(
            FirestoreServiceImpl.ADDRESS_FIELD to myId,
            FirestoreServiceImpl.EXPLORE_COLLECTION to link,
            FirestoreServiceImpl.USERNAME_FIELD to _username.value,
            FirestoreServiceImpl.PHOTO_FIELD to _photo.value,
            FirestoreServiceImpl.DESCRIPTION_FIELD to "text",
        )

        val uploadRequest = OneTimeWorkRequestBuilder<PhotoStoryWorker>()
            .setInputData(inputData)
            .build()

        WorkManager.getInstance(application).enqueue(uploadRequest)
        popUp()
    }
    private fun saveChatPhoto(link: String, popUp: () -> Unit) {
        val inputData = workDataOf(
            FirestoreServiceImpl.CAFE_COLLECTION to myId.toString(),
            FirestoreServiceImpl.EXPLORE_COLLECTION to link.toString(),
            FirestoreServiceImpl.USERNAME_FIELD to chatId.value.toString(),
        )

        val uploadRequest = OneTimeWorkRequestBuilder<PhotoChatWorker>()
            .setInputData(inputData)
            .build()

        WorkManager.getInstance(application).enqueue(uploadRequest)
        popUp()
    }
}
