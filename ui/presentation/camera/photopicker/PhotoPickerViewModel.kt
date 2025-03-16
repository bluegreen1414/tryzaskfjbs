package com.kapirti.social_chat_food_video.ui.presentation.camera.photopicker

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
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
import com.kapirti.social_chat_food_video.ui.presentation.camera.func.VideoChatWorker
import com.kapirti.social_chat_food_video.ui.presentation.camera.func.VideoExploreWorker
import com.kapirti.social_chat_food_video.ui.presentation.camera.func.VideoStoryWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class PhotoPickerViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val accountService: AccountService,
    private val firestoreService: FirestoreService,
    private val storageService: StorageService,
    private val cameraTypeRepository: CameraTypeRepository,
    private val contentResolver: ContentResolver,
    private val savedStateHandle: SavedStateHandle,
    logService: LogService,
) : KapirtiViewModel(logService) {
    private val chatIdArg: String by lazy {
        savedStateHandle.get<String?>("chatId") ?: throw IllegalArgumentException("chatId is null")
    }


    private val _cameraType = mutableStateOf<String?>(null)
    val cameraType: String?
        get() = _cameraType.value

    var uiState = mutableStateOf(PhotoPickerUiState())
        private set

    private val text
        get() = uiState.value.text

    private val _selfieUri = mutableStateOf<Uri?>(null)
    val selfieUri
        get() = _selfieUri.value

    private val _bitmap = mutableStateOf<Bitmap?>(null)
    val bitmap
        get() = _bitmap.value

    val myId = accountService.currentUserId
    private val _username = mutableStateOf<String?>(null)
    private val _photo = mutableStateOf<String?>(null)


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

    fun save(onPhotoPicked: () -> Unit) {
        launchCatching {
            onIsDoneBtnWorking(true)

            if (text.isBlank()) {
                onIsErrorTextChange(true)
                onIsDoneBtnWorking(false)
                return@launchCatching
            }

            if (_photo.value.isNullOrEmpty()) {
                onIsErrorPhotoChange(true)
                onIsDoneBtnWorking(false)
                return@launchCatching
            }


            _selfieUri.value?.let {
                val mimeType = context.contentResolver.getType(it)
                if (mimeType != null) {
                    when {
                        mimeType.startsWith("image/") -> {
                            saveImage(onPhotoPicked)
                        }

                        mimeType.startsWith("video/") -> {
                            saveVideo(onPhotoPicked)
                        }

                        else -> {
                            Toast.makeText(context, "Unsupported file type", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun saveImage(onPhotoPicked: () -> Unit) {
        launchCatching {
            _selfieUri.value?.let {
                if (Build.VERSION.SDK_INT < 28) {
                    _bitmap.value =
                        MediaStore.Images.Media.getBitmap(context.contentResolver, it)
                    saveImageBody(onPhotoPicked)
                } else {
                    val source = ImageDecoder.createSource(context.contentResolver, it)
                    _bitmap.value = ImageDecoder.decodeBitmap(source)
                    saveImageBody(onPhotoPicked)
                }
            }
        }
    }
    private fun saveImageBody(onPhotoPicked: () -> Unit) {
        launchCatching {
            _bitmap.value?.let { bitmapNew ->
                val compressedPhoto = compressPhoto(bitmap!!)
                val randomUid = UUID.randomUUID().toString()

                storageService.savePhoto(compressedPhoto, uid = randomUid)
                val link = storageService.getPhoto(randomUid)


                if (_cameraType.value == FirestoreServiceImpl.EXPLORE_COLLECTION){ saveExplorePhoto(link, onPhotoPicked) }
                else if(_cameraType.value == FirestoreServiceImpl.STORY_COLLECTION){ saveStoryPhoto(link, onPhotoPicked) }
                else if(_cameraType.value == FirestoreServiceImpl.CHAT_COLLECTION){ saveChatPhoto(link, onPhotoPicked) }
            }
        }
    }
    private fun saveExplorePhoto(link: String, popUp: () -> Unit) {
        val inputData = workDataOf(
            FirestoreServiceImpl.ADDRESS_FIELD to myId,
            FirestoreServiceImpl.EXPLORE_COLLECTION to link,
            FirestoreServiceImpl.USERNAME_FIELD to _username.value,
            FirestoreServiceImpl.PHOTO_FIELD to _photo.value,
            FirestoreServiceImpl.DESCRIPTION_FIELD to text,
        )

        val uploadRequest = OneTimeWorkRequestBuilder<PhotoExploreWorker>()
            .setInputData(inputData)
            .build()

        WorkManager.getInstance(context).enqueue(uploadRequest)
        popUp()
    }
    private fun saveStoryPhoto(link: String, popUp: () -> Unit) {
        val inputData = workDataOf(
            FirestoreServiceImpl.ADDRESS_FIELD to myId,
            FirestoreServiceImpl.EXPLORE_COLLECTION to link,
            FirestoreServiceImpl.USERNAME_FIELD to _username.value,
            FirestoreServiceImpl.PHOTO_FIELD to _photo.value,
            FirestoreServiceImpl.DESCRIPTION_FIELD to text,
        )

        val uploadRequest = OneTimeWorkRequestBuilder<PhotoStoryWorker>()
            .setInputData(inputData)
            .build()

        WorkManager.getInstance(context).enqueue(uploadRequest)
        popUp()
    }
    private fun saveChatPhoto(link: String, popUp: () -> Unit) {
        val inputData = workDataOf(
            FirestoreServiceImpl.CAFE_COLLECTION to myId.toString(),
            FirestoreServiceImpl.EXPLORE_COLLECTION to link.toString(),
            FirestoreServiceImpl.USERNAME_FIELD to chatIdArg.toString(),
        )

        val uploadRequest = OneTimeWorkRequestBuilder<PhotoChatWorker>()
            .setInputData(inputData)
            .build()

        WorkManager.getInstance(context).enqueue(uploadRequest)
        popUp()
    }

    @OptIn(UnstableApi::class)
    private fun saveVideo(onPhotoPicked: () -> Unit) {
        launchCatching {
            val randomUid = UUID.randomUUID().toString()

            if (_cameraType.value == FirestoreServiceImpl.EXPLORE_COLLECTION) {
                saveExploreVideo(randomUid, onPhotoPicked)
            } else if (_cameraType.value == FirestoreServiceImpl.STORY_COLLECTION) {
                saveStoryVideo(randomUid, onPhotoPicked)
            }
            else if(_cameraType.value == FirestoreServiceImpl.CHAT_COLLECTION){ saveChatVideo(randomUid, onPhotoPicked) }
        }
    }
    private fun saveExploreVideo(randomUid: String, popUp: () -> Unit) {
        val inputData = workDataOf(
            FirestoreServiceImpl.CHAT_COLLECTION to randomUid.toString(),
            FirestoreServiceImpl.CAFE_COLLECTION to myId.toString(),
            FirestoreServiceImpl.EXPLORE_COLLECTION to _selfieUri.value.toString(),
            FirestoreServiceImpl.USERNAME_FIELD to _username.value.toString(),
            FirestoreServiceImpl.PHOTO_FIELD to _photo.value.toString(),
            FirestoreServiceImpl.DESCRIPTION_FIELD to text.toString(),
        )

        val uploadRequest = OneTimeWorkRequestBuilder<VideoExploreWorker>()
            .setInputData(inputData)
            .build()

        WorkManager.getInstance(context).enqueue(uploadRequest)
        popUp()
    }
    private fun saveStoryVideo(randomUid: String, popUp: () -> Unit) {
        val inputData = workDataOf(
            FirestoreServiceImpl.CHAT_COLLECTION to randomUid.toString(),
            FirestoreServiceImpl.CAFE_COLLECTION to myId.toString(),
            FirestoreServiceImpl.EXPLORE_COLLECTION to _selfieUri.value.toString(),
            FirestoreServiceImpl.USERNAME_FIELD to _username.value.toString(),
            FirestoreServiceImpl.PHOTO_FIELD to _photo.value.toString(),
            FirestoreServiceImpl.DESCRIPTION_FIELD to text.toString(),
        )

        val uploadRequest = OneTimeWorkRequestBuilder<VideoStoryWorker>()
            .setInputData(inputData)
            .build()

        WorkManager.getInstance(context).enqueue(uploadRequest)
        popUp()
    }
    private fun saveChatVideo(randomUid: String, popUp: () -> Unit) {
        val inputData = workDataOf(
            FirestoreServiceImpl.CHAT_COLLECTION to randomUid.toString(),
            FirestoreServiceImpl.CAFE_COLLECTION to myId.toString(),
            FirestoreServiceImpl.EXPLORE_COLLECTION to _selfieUri.value.toString(),
            FirestoreServiceImpl.USERNAME_FIELD to chatIdArg.toString(),
        )

        val uploadRequest = OneTimeWorkRequestBuilder<VideoChatWorker>()
            .setInputData(inputData)
            .build()

        WorkManager.getInstance(context).enqueue(uploadRequest)
        popUp()
    }


    /**  private fun savevideo(onPhotoPicked: () -> Unit) {
        launchCatching {
             val randomUid = UUID.randomUUID().toString()
            storageService.saveVideo(imageUri, randomUid)
            val link = storageService.getVideo(randomUid)


            firestoreService.saveChatRoomMessage(
            roomId = chatIdArg,
            SaveChatMessage(
            mediaUri = link,
            mediaMimeType = ExploreMediaType.VIDEO.toString(),
            senderId = accountService.currentUserId,
            timestamp = Timestamp.now()
            )
            )
            onPhotoPicked()
            }*/
/**
    fun onPhotoPicked(imageUri: Uri, onPhotoPicked: () -> Unit) {
        launchCatching {
            /**            contentResolver.takePersistableUriPermission(
            imageUri,
            Intent.FLAG_GRANT_READ_URI_PERMISSION
            )*/

            val mimeType = context.contentResolver.getType(imageUri)
            if (mimeType != null) {
                when {
                    mimeType.startsWith("image/") -> {
                        saveImage(imageUri, onPhotoPicked)
                    }

                    mimeType.startsWith("video/") -> {
                        saveVideo(imageUri, onPhotoPicked)
                    }

                    else -> {}
                }
            }
        }
    }

    private fun saveImage(imageUri: Uri, onPhotoPicked: () -> Unit){
        launchCatching {
            if (Build.VERSION.SDK_INT < 28) {
                _bitmap.value = MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
                saveProductBody(onPhotoPicked)
            } else {
                val source = ImageDecoder.createSource(context.contentResolver, imageUri)
                _bitmap.value = ImageDecoder.decodeBitmap(source)
                saveProductBody(onPhotoPicked)
            }
        }
    }

    private fun saveProductBody(onPhotoPicked: () -> Unit) {
        launchCatching {
            _bitmap.value?.let { bitmapNew ->
                val kucukBitmap = kucukBitmapOlustur(bitmapNew!!, 300)
                val outputStream = ByteArrayOutputStream()
                kucukBitmap.compress(Bitmap.CompressFormat.PNG, 50, outputStream)
                val byteDizisi = outputStream.toByteArray()
                val randomUid = UUID.randomUUID().toString()

                storageService.savePhoto(byteDizisi, uid = randomUid)
                val link = storageService.getPhoto(randomUid)


                firestoreService.saveChatRoomMessage(
                    roomId = chatIdArg,
                    SaveChatMessage(
                        mediaUri = link,
                        mediaMimeType = ExploreMediaType.PHOTO.toString(),
                        senderId = accountService.currentUserId,
                        timestamp = Timestamp.now()
                    )
                )
                onPhotoPicked()
            }
        }
    }
    private fun saveVideo(imageUri: Uri, onPhotoPicked: () -> Unit){
        launchCatching {
            val randomUid = UUID.randomUUID().toString()
            storageService.saveVideo(imageUri, randomUid)
            val link = storageService.getVideo(randomUid)


            firestoreService.saveChatRoomMessage(
                roomId = chatIdArg,
                SaveChatMessage(
                    mediaUri = link,
                    mediaMimeType = ExploreMediaType.VIDEO.toString(),
                    senderId = accountService.currentUserId,
                    timestamp = Timestamp.now()
                )
            )
            onPhotoPicked()
        }
    }
*/
    fun onSelfieResponse(uri: Uri) { _selfieUri.value = uri }
    private fun onIsDoneBtnWorking(newValue: Boolean) { uiState.value = uiState.value.copy(isDoneBtnWorking = newValue) }
    fun onTextChange(newValue: String) { uiState.value = uiState.value.copy(text = newValue) }
    private fun onIsErrorTextChange(newValue: Boolean) { uiState.value = uiState.value.copy(isErrorText = newValue) }
    private fun onIsErrorPhotoChange(newValue: Boolean) { uiState.value = uiState.value.copy(isErrorPhotos = newValue) }
}





/**

            firestoreService.getChatRoomMessageReference(r.id).add(chatMessageText)
                .addOnSuccessListener {
                    _input.value = ""
//                    notificationHelper.showNotification(
//                        User(name = "notif name"), listOf(ChatMessage(message = input)), true
//                    )

                    //FirebaseMessaging.getInstance().

                    android.util.Log.d(
                        "myTag",
                        "about to send message to fcm server, my id is : ${_myId}",
                    )
                    android.util.Log.d(
                        "myTag",
                        "about to send message to fcm server, the user who has this token should receive the messsage : ${otherUser!!.fcmToken}",
                    )
                    sendMessageToFCMserver(
                        r.id,
                        otherUser!!.fcmToken,
                        chatMessageText,
                        "me.email.orEmpty()",
                        mediaUri,
                        mediaMimeType,
                    )

                }
        }
    }
}
private fun sendMessageToFCMserver(roomId: String, receiverToken: String, message: ChatMessageSaveText, senderName: String, mediaUri: String?, mediaMimeType: String?) {
    launchCatching {
        val messageDto = SendMessageDto(
            from = roomId,
            to = receiverToken,
            notification = NotificationBody(
                title = senderName,
                body = message.text,
                mediaUri = mediaUri,
                mediaMimeType = mediaMimeType,
            ),
        )
        try {
            android.util.Log.d("myTag","sending api request now, is emulator : $isEmulator, build hardware : ${Build.HARDWARE}")

            api.sendMessage(messageDto)

            //state = state.copy(messageText = "")
        } catch (e: Exception) {
            android.util.Log.d("myTag","exception here : ${e.message}")
            e.printStackTrace()
        }


    }
}
*/
