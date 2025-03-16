package com.kapirti.social_chat_food_video.ui.presentation.chat

import android.content.Context
import android.os.Build
import androidx.compose.runtime.mutableStateOf
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.google.firebase.Timestamp
import com.kapirti.social_chat_food_video.BuildConfig
import com.kapirti.social_chat_food_video.common.stateInUi
import com.kapirti.social_chat_food_video.core.constants.SelectType
import com.kapirti.social_chat_food_video.core.datastore.CameraTypeRepository
import com.kapirti.social_chat_food_video.model.Block
import com.kapirti.social_chat_food_video.model.GetChatMessage
import com.kapirti.social_chat_food_video.model.Report
import com.kapirti.social_chat_food_video.model.SaveChatMessage
import com.kapirti.social_chat_food_video.model.service.AccountService
import com.kapirti.social_chat_food_video.model.service.FirestoreService
import com.kapirti.social_chat_food_video.model.service.LogService
import com.kapirti.social_chat_food_video.model.service.impl.FirestoreServiceImpl
import com.kapirti.social_chat_food_video.ui.presentation.KapirtiViewModel
import com.kapirti.social_chat_food_video.ui.presentation.camera.func.VideoStoryWorker
import com.kapirti.social_chat_food_video.ui.presentation.chat.work.LastMessageWorker
import com.kapirti.social_chat_food_video.ui.presentation.notification.NotificationHelper
import com.kapirti.social_chat_food_video.ui.presentation.notification.components.FcmApi
import com.kapirti.social_chat_food_video.ui.presentation.notification.components.NotificationBody
import com.kapirti.social_chat_food_video.ui.presentation.notification.components.SendMessageDto
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

@HiltViewModel
class ChatViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val firestoreService: FirestoreService,
    private val accountService: AccountService,
    private val cameraTypeRepository: CameraTypeRepository,
    private val notificationHelper: NotificationHelper,
    logService: LogService
) : KapirtiViewModel(logService) {
    private val chatId = MutableStateFlow("")
    private val _myId = accountService.currentUserId
    private val _mePhoto = mutableStateOf<String?>(null)
    private val _meUsername = mutableStateOf<String?>(null)
    val showActionDialog = mutableStateOf(false)
    var showBlockDialog = mutableStateOf(false)
    var showReportDialog = mutableStateOf(false)


    var uiState = mutableStateOf(ChatUiState())
        private set


    private val _partnerId = mutableStateOf<String?>(null)
    val partnerId: String?
        get() = _partnerId.value

    private val _partnerPhoto = mutableStateOf<String?>(null)
    val partnerPhoto: String?
        get() = _partnerPhoto.value

    private val _partnerUsername = mutableStateOf<String?>(null)
    val partnerUsername: String?
        get() = _partnerUsername.value

    private val _isTyping = mutableStateOf<Boolean?>(false)
    val isTyping: Boolean?
        get() = _isTyping.value

    private val _online = mutableStateOf<Boolean?>(null)
    val online: Boolean?
        get() = _online.value

    private val _lastSeen = mutableStateOf<Timestamp?>(null)
    val lastSeen: Timestamp?
        get() = _lastSeen.value


    init {
        notificationHelper.setUpNotificationChannels()
        getMeInfo()
    }

    private val isEmulator = Build.HARDWARE.equals("ranchu")

    private val api: FcmApi = Retrofit.Builder()
        .baseUrl(if (isEmulator) BuildConfig.NOTIFICATION_SERVER_EMULATOR else BuildConfig.NOTIFICATION_SERVER_REAL_DEVICE)
        .addConverterFactory(MoshiConverterFactory.create())
        .build().create()


    fun setChatId(chatId: String) {
        this.chatId.value = chatId
        fetchConversation(chatId)
    }

    private fun fetchConversation(roomId: String) = launchCatching {
        val chatRoom = firestoreService.getChatRoomFromChatRoom(roomId)

        val otherUserId = chatRoom!!.userIds.filterNot { it == _myId }.first()
        val otherUser = firestoreService.getUser(otherUserId)

        uiState.value =
            uiState.value.copy(chatRoom = chatRoom, partnerUserName = otherUser?.name.orEmpty())
//        if (chatRoom == null) {
//            val newChatRoom = ChatRoom(
//                roomId,
//                arrayListOf(firstUserId, secondUserId),
//                Timestamp.now(),
//                ""
//            )
//            firestoreService.setChatRoom(chatId, newChatRoom)
//            uiState.value = uiState.value.copy(chatRoom = newChatRoom)
//
//        } else {
//            uiState.value = uiState.value.copy(chatRoom = chatRoom)
//        }

        observeChatMessages(roomId)
        getUserInfo(otherUserId)
        getChatStatus(otherUserId)
//        obserUserStateChanges(roomId, otherUserId)
    }

    private fun observeChatMessages(chatId: String) = launchCatching {
        firestoreService.getChatMessage(chatId).collectLatest {
            uiState.value = uiState.value.copy(
                messages = it.map {
                    GetChatMessage(
                        id = it.id,
                        senderId = it.senderId,
                        text = it.text,
                        mediaUri = it.mediaUri,
                        mediaMimeType = it.mediaMimeType,
                        isIncoming = it.senderId != _myId,
                        timestamp = it.timestamp,
                    )
                }
            )
        }
    }

    private fun getChatStatus(id: String) {
        launchCatching {
            firestoreService.getChatStatus(id)?.let {
                _isTyping.value = it.isTyping
            }
        }
    }

    private fun getUserInfo(userId: String) {
        launchCatching {
            _partnerId.value = userId
            firestoreService.getUser(userId)?.let { me ->
                when (me.type) {
                    SelectType.FLIRT -> {
                        firestoreService.getUserFlirt(country = me.country, userId)?.let {
                            _partnerUsername.value = it.username
                            _partnerPhoto.value = it.photo
                            _online.value = it.online
                            _lastSeen.value = it.lastSeen
                        }
                    }

                    SelectType.MARRIAGE -> {
                        firestoreService.getUserMarriage(country = me.country, userId)?.let {
                            _partnerUsername.value = it.username
                            _partnerPhoto.value = it.photo
                            _online.value = it.online
                            _lastSeen.value = it.lastSeen
                        }
                    }

                    SelectType.LANGUAGE_PRACTICE -> {
                        firestoreService.getUserLP(motherTongue = me.motherTongue, userId)?.let {
                            _partnerUsername.value = it.username
                            _partnerPhoto.value = it.photo
                            _online.value = it.online
                            _lastSeen.value = it.lastSeen
                        }
                    }

                    SelectType.HOTEL -> {
                        firestoreService.getHotel(country = me.country, userId)?.let {
                            _partnerUsername.value = it.username
                            _partnerPhoto.value = it.photo
                            _online.value = it.online
                            _lastSeen.value = it.lastSeen
                        }
                    }

                    SelectType.RESTAURANT -> {
                        firestoreService.getRestaurant(country = me.country, userId)?.let {
                            _partnerUsername.value = it.username
                            _partnerPhoto.value = it.photo
                            _online.value = it.online
                            _lastSeen.value = it.lastSeen
                        }
                    }

                    SelectType.CAFE -> {
                        firestoreService.getCafe(country = me.country, userId)?.let {
                            _partnerUsername.value = it.username
                            _partnerPhoto.value = it.photo
                            _online.value = it.online
                            _lastSeen.value = it.lastSeen
                        }
                    }

                    else -> {}
                }
            }
            removeUnread()
        }
    }

    private fun getMeInfo() {
        launchCatching {
            firestoreService.getUser(accountService.currentUserId)?.let { me ->
                when (me.type) {
                    SelectType.FLIRT -> {
                        firestoreService.getUserFlirt(
                            country = me.country,
                            accountService.currentUserId
                        )?.let {
                            _meUsername.value = it.username
                            _mePhoto.value = it.photo
                        }
                    }

                    SelectType.MARRIAGE -> {
                        firestoreService.getUserMarriage(
                            country = me.country,
                            accountService.currentUserId
                        )?.let {
                            _meUsername.value = it.username
                            _mePhoto.value = it.photo
                        }
                    }

                    SelectType.LANGUAGE_PRACTICE -> {
                        firestoreService.getUserLP(
                            motherTongue = me.motherTongue,
                            accountService.currentUserId
                        )?.let {
                            _meUsername.value = it.username
                            _mePhoto.value = it.photo
                        }
                    }

                    SelectType.HOTEL -> {
                        firestoreService.getHotel(
                            country = me.country,
                            accountService.currentUserId
                        )?.let {
                            _meUsername.value = it.username
                            _mePhoto.value = it.photo
                        }
                    }

                    SelectType.RESTAURANT -> {
                        firestoreService.getRestaurant(
                            country = me.country,
                            accountService.currentUserId
                        )?.let {
                            _meUsername.value = it.username
                            _mePhoto.value = it.photo
                        }
                    }

                    SelectType.CAFE -> {
                        firestoreService.getCafe(country = me.country, accountService.currentUserId)
                            ?.let {
                                _meUsername.value = it.username
                                _mePhoto.value = it.photo
                            }
                    }

                    else -> {}
                }
            }
        }
    }

    private fun removeUnread() {
        launchCatching {
            firestoreService.updateUserChatUnread(_partnerId.value ?: "")
        }
    }


    private val _input = MutableStateFlow("")
    val input: StateFlow<String> = _input
    private var inputPrefilled = false

    val sendEnabled = _input.map(::isInputValid).stateInUi(false)

    fun updateInput(input: String, chatId: String) {
        _input.value = input
        launchCatching {
            // if (input.isNotBlank()) {

            val inputData = workDataOf(
                FirestoreServiceImpl.CHAT_COLLECTION to _partnerId.value,
                FirestoreServiceImpl.CAFE_COLLECTION to _myId,
            )

            val uploadRequest = OneTimeWorkRequestBuilder<VideoStoryWorker>()
                .setInputData(inputData)
                .build()

            WorkManager.getInstance(context).enqueue(uploadRequest)
        }
    }

    //    firestoreService.updateUserChatTyping(id = _partnerId.value ?: "", true)

    //          delay(10000L)
//            firestoreService.updateUserChatTyping(id = _partnerId.value ?: "", false)

    //firestoreService.setUserTyping(chatId)
    // } else {
    //
//                firestoreService.clearUserTyping(chatId)
    //}

    /**
    fun send() {
    val input = _input.value
    if (!isInputValid(input)) return
    sendMessage(text = input)
    }*/

    fun send(content: String) {
        if (!isInputValid(content)) return
        sendMessage(text = content)
    }

    private fun sendMessage(
        text: String? = null,
        mediaUri: String? = null,
        mediaMimeType: String? = null,
    ) {
        val chatId = chatId.value
        if (chatId.isEmpty()) return

        launchCatching {
            val room = firestoreService.getChatRoomFromChatRoom(chatId)
            val otherUserId = room!!.userIds.filterNot { it == _myId }.first()
            val otherUser = firestoreService.getUser(otherUserId)

            room.lastMessageTime = Timestamp.now()
            room.lastMessageSenderId = _myId
            room.lastMessage = text.orEmpty()
            room.mediaUri = mediaUri
            room.mediaMimeType = mediaMimeType
            room.let { r ->

//                firestoreService.saveChatRoomFromChatRoom(chatRoomId = r.id, r)
//                firestoreService.setChatRoom(r.id, r)

                val chatMessageText = SaveChatMessage(
                    text = text.orEmpty(),
                    senderId = _myId,
                    timestamp = Timestamp.now(),
                )


                firestoreService.getChatRoomMessageReference(r.id).add(chatMessageText)
                    .addOnSuccessListener {
                        val inputData = workDataOf(
                            FirestoreServiceImpl.CAFE_COLLECTION to _myId,
                            FirestoreServiceImpl.CHAT_COLLECTION to _partnerId.value,
                            FirestoreServiceImpl.EXPLORE_COLLECTION to _input.value,
                        )

                        val uploadRequest = OneTimeWorkRequestBuilder<LastMessageWorker>()
                            .setInputData(inputData)
                            .build()

                        WorkManager.getInstance(context).enqueue(uploadRequest)



                        _input.value = ""
//                    notificationHelper.showNotification(
//                        User(name = "notif name"), listOf(ChatMessage(message = input)), true
//                    )

                        //FirebaseMessaging.getInstance().


                        sendMessageToFCMserver(
                            r.id,
                            otherUser!!.fcmToken,
                            chatMessageText,
                            _meUsername.value ?: "KAPIRTI",
                            mediaUri,
                            mediaMimeType,
                        )
                    }
            }
        }
    }

    private fun sendMessageToFCMserver(
        roomId: String,
        receiverToken: String,
        message: SaveChatMessage,
        senderName: String,
        mediaUri: String?,
        mediaMimeType: String?
    ) {
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
                api.sendMessage(messageDto)
            } catch (e: Exception) {
                android.util.Log.d("myTag", "exception here : ${e.message}")
                e.printStackTrace()
            }
        }
    }


    fun onActionClick() {
        showActionDialog.value = true
    }

    fun onBlockClick() {
        showActionDialog.value = false
        showBlockDialog.value = true
    }

    fun onReportClick() {
        showActionDialog.value = false
        showReportDialog.value = true
    }


    fun onReportButtonClick(popUpScreen: () -> Unit) {
        val date = Timestamp.now()
        launchCatching {
            firestoreService.report(
                uid = accountService.currentUserId,
                partnerUid = _partnerId.value ?: "",
                Report(
                    id = accountService.currentUserId,
                    displayName = _meUsername.value ?: "",
                    photo = _mePhoto.value ?: "",
                    dateOfCreation = date
                )
            )
            onBlockButtonClick(popUpScreen = popUpScreen)
        }
    }

    fun onBlockButtonClick(popUpScreen: () -> Unit) {
        launchCatching {
            firestoreService.block(
                uid = _myId,
                partnerUid = _partnerId.value ?: "",
                block = Block(
                    id = _partnerId.value ?: "",
                    nickname = _partnerUsername.value ?: "",
                )
            )
            firestoreService.block(
                uid = _partnerId.value ?: "",
                partnerUid = accountService.currentUserId,
                block = Block(
                    id = _myId,
                    nickname = _meUsername.value ?: "",
                )
            )
            firestoreService.deleteChat(chatId = chatId.value ?: "")
            firestoreService.deleteUserChat(who = accountService.currentUserId, chatId.value ?: "")
            firestoreService.deleteUserChat(who = _partnerId.value ?: "", chatId.value ?: "")
            popUpScreen()
        }
    }

    fun callFriend() = launchCatching {
        //sendMessage(chatId, input, null, null)

        val room = uiState.value.chatRoom

        val otherUserId = room!!.userIds.filterNot { it == _myId }.first()
        val otherUser = firestoreService.getUser(otherUserId)
        otherUser?.let {
            sendCallRequestToFCMserver(
                room.id,
                otherUser.fcmToken,
                _meUsername.value.orEmpty()
            )//me.email.orEmpty())

        }

        //notificationHelper.showCallNotification(otherUser)
    }


    private fun sendCallRequestToFCMserver(
        roomId: String,
        receiverToken: String,
        senderName: String
    ) {
        launchCatching {
            val messageDto = SendMessageDto(
                from = roomId,
                to = receiverToken,
                notification = NotificationBody(
                    title = senderName,
                    body = "Someone is calling...",
                ),
            )
            try {
                android.util.Log.d(
                    "myTag",
                    "sending api request now, is emulator : $isEmulator, build hardware : ${Build.HARDWARE}"
                )

                api.sendCallRequest(messageDto)

                //state = state.copy(messageText = "")
            } catch (e: Exception) {
                android.util.Log.d("myTag", "exception here : ${e.message}")
                e.printStackTrace()
            }


        }
    }

    fun onCameraClick(onCameraClick: () -> Unit){
        launchCatching {
            cameraTypeRepository.saveState(FirestoreServiceImpl.CHAT_COLLECTION)
            onCameraClick()
        }
    }
}

    //var state by mutableStateOf(ChatState())
    //    private set




//    fun onRemoteTokenChange(newToken: String) {
//        state = state.copy(
//            remoteToken = newToken
//        )
//    }
//
//    fun onSubmitRemoteToken() {
//        state = state.copy(
//            isEnteringToken = false
//        )
//    }
//
//    fun onMessageChange(message: String) {
//        state = state.copy(
//            messageText = message
//        )
//    }
/**

    fun fetchConversation(firstUserId: String, secondUserId: String) = viewModelScope.launch {
        val chatId = FirestoreServiceImpl.getChatRoomId(firstUserId, secondUserId)
        val chatRoom = firestoreService.getChatRoom(chatId)
        if (chatRoom == null) {
            val newChatRoom = ChatRoom(
                chatId,
                arrayListOf(firstUserId, secondUserId),
                Timestamp.now(),
                "",
            )
            firestoreService.setChatRoom(chatId, newChatRoom)
            uiState.value = uiState.value.copy(chatRoom = newChatRoom)

        } else {
            uiState.value = uiState.value.copy(chatRoom = chatRoom)
        }

        observeChatMessages(chatId)

    }






    fun obserUserStateChanges(roomId: String, otherUserId: String) = viewModelScope.launch {
        firestoreService.observeOtherChatState(otherUserId)
            .collectLatest { (onlineState, typingTo) ->
                if (typingTo == roomId) {
                    uiState.value = uiState.value.copy(otherUserChatState = "Typing..")
                } else if (onlineState == "online") {
                    uiState.value = uiState.value.copy(otherUserChatState = "Online")
                } else if (isNumeric(onlineState)) {

//                    val now = Instant.now() // Capture the current moment as seen in UTC.
//                    val then = now.minus(8L, ChronoUnit.HOURS).minus(8L, ChronoUnit.MINUTES)
//                        .minus(8L, ChronoUnit.SECONDS)
//                    val d: Duration = Duration.between(then, now)

                    //val x = Duration.parse(onlineState).toString()
                    //Date(onlineState).
                    //Date(onlineState)

                    val lastTime : Long = onlineState.toLong()
                    android.util.Log.d("myTag7","last time : $lastTime, current time : ${System.currentTimeMillis()}")
                    android.util.Log.d("myTag7","last time : $lastTime, current time : ${
                        Timestamp.now().toDate().time}")
//                    //Duration.
                    //val diff = lastTime - Date().time

                    //val duration = diff.toDuration(DurationUnit.MILLISECONDS)

//
//                    val diffDuration = diff.toDuration(DurationUnit.MILLISECONDS)
                    uiState.value =
                        uiState.value.copy(
                            otherUserChatState = "Last seen : ${
                                getTimeAgoFormat3(
                                    //System.currentTimeMillis() - 70000, // works this way
                                    System.currentTimeMillis() - lastTime,
                                    System.currentTimeMillis(),
                                )
                            }",
                        )
                }
            }
    }

    fun isNumeric(str: String) = str.isNotBlank() && str.all { it in '0'..'9' }


    fun getTimeAgoFormat(timestamp: Long): String {
        return DateUtils.getRelativeTimeSpanString(timestamp).toString()
    }

    fun getTimeAgoFormat2(timestamp: Long, now: Long, minResolution: Long): String {
        return DateUtils.getRelativeTimeSpanString(timestamp, now, minResolution).toString()
    }

    fun getTimeAgoFormat3(oldDate: Long, currentDateLong: Long) : String {
        return DateUtils.getRelativeTimeSpanString(
            oldDate, currentDateLong,
            0L, DateUtils.FORMAT_ABBREV_ALL
        ).toString()
    }



    fun sendMediaMessage(uriText: String, uriMimeType: String) {
        android.util.Log.d("myTag","try sending uri message : $uriText, mime type : $uriMimeType")
        //sendMessage(mediaUri = uriText, mediaMimeType = uriMimeType)
        val videoSampleLink = "https://firebasestorage.googleapis.com/v0/b/baret-ca108.appspot.com/o/Photos%2FOvESQrtsIBZdc93sgo5xELPpvnv2%2Fc8d7cb08-f6b4-4210-9669-a8f6a80fdec3.mp4?alt=media&token=b321a0f8-378a-4a65-95be-f71672d69241"
        val photoSampleLink = "https://firebasestorage.googleapis.com/v0/b/baret-ca108.appspot.com/o/Photos%2FOvESQrtsIBZdc93sgo5xELPpvnv2%2F4615ac85-d05b-449e-beff-72f7d32b7efc.jpg?alt=media&token=a338e94a-5e9a-4c77-bec7-76e7d5aa45a1"

        android.util.Log.d("myTag","uri mime type is : $uriMimeType")

        if (uriMimeType.contains("PHOTO")) {
            android.util.Log.d("myTag","this is image, send sample link")
            sendMessage(mediaUri = photoSampleLink, mediaMimeType = uriMimeType)

        } else if (uriMimeType.contains("VIDEO")) {
            android.util.Log.d("myTag","this is video, send sample link")
            sendMessage(mediaUri = videoSampleLink, mediaMimeType = uriMimeType)
        }
    }

    fun prefillInput(input: String) {
        if (inputPrefilled) return
        inputPrefilled = true
        //updateInput(input)
    }




}
*/

private fun isInputValid(input: String): Boolean = input.isNotBlank()
