package com.kapirti.social_chat_food_video.ui.presentation.userprofile

import android.content.Context
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.media3.common.util.UnstableApi
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.google.firebase.firestore.FirebaseFirestore
import com.kapirti.social_chat_food_video.core.constants.SelectType
import com.kapirti.social_chat_food_video.model.ChatRoomFromChatRoom
import com.kapirti.social_chat_food_video.model.User
import com.kapirti.social_chat_food_video.model.service.AccountService
import com.kapirti.social_chat_food_video.model.service.FirestoreService
import com.kapirti.social_chat_food_video.model.service.LogService
import com.kapirti.social_chat_food_video.model.service.impl.FirestoreServiceImpl
import com.kapirti.social_chat_food_video.ui.presentation.KapirtiViewModel
import com.kapirti.social_chat_food_video.model.ExploreUserItem
import com.kapirti.social_chat_food_video.ui.Route
import com.kapirti.social_chat_food_video.ui.presentation.userprofile.func.SaveChatRoomFromUserChatRoom
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@UnstableApi
@HiltViewModel
class UserProfileViewModel @Inject constructor(
    @ApplicationContext private val application: Context,
    private val firestoreService: FirestoreService,
    private val accountService: AccountService,
    logService: LogService,
) : KapirtiViewModel(logService) {
    private val ff = FirebaseFirestore.getInstance()
    var media by mutableStateOf<List<ExploreUserItem>>(emptyList())


    var uiState = mutableStateOf(UserProfileUiState())
        private set


    var accountInfo = mutableStateOf(UserProfileAccountUiState())
        private set

    private val partnerId
        get() = accountInfo.value.userId

    private val partnerPhoto
        get() = accountInfo.value.photo

    private val partnerUsername
        get() = accountInfo.value.username

    private val mePhoto
        get() = accountInfo.value.mePhoto

    private val meUsername
        get() = accountInfo.value.meUsername

    private val _meId = accountService.currentUserId

    init {
        launchCatching {
            firestoreService.getUser(_meId)?.let { me ->
                when (me.type) {
                    SelectType.FLIRT -> {
                        firestoreService.getUserFlirt(country = me.country, _meId)?.let {
                            onMeInfo(it.photo, it.username)
                        }
                    }

                    SelectType.MARRIAGE -> {
                        firestoreService.getUserMarriage(country = me.country, _meId)?.let {
                            onMeInfo(it.photo, it.username)
                        }
                    }

                    SelectType.LANGUAGE_PRACTICE -> {
                        firestoreService.getUserLP(motherTongue = me.motherTongue, _meId)?.let {
                            onMeInfo(it.photo, it.username)
                        }
                    }

                    SelectType.HOTEL -> {
                        firestoreService.getHotel(country = me.country, _meId)?.let {
                            onMeInfo(it.photo, it.username)
                        }
                    }

                    SelectType.RESTAURANT -> {
                        firestoreService.getRestaurant(country = me.country, _meId)?.let {
                            onMeInfo(it.photo, it.username)
                        }
                    }

                    SelectType.CAFE -> {
                        firestoreService.getCafe(country = me.country, _meId)?.let {
                            onMeInfo(it.photo, it.username)
                        }
                    }

                    else -> {}
                }
            }
        }
    }


    fun getUser(userId: String) = launchCatching {
        val x = firestoreService.getUser(userId)
        uiState.value = uiState.value.copy(user = x)
        accountInfo.value = accountInfo.value.copy(userId = userId)
        getUserExplore(userId)
        getUserInfo(x ?: User())
    }

    fun clearRoomId() {
        uiState.value = uiState.value.copy(navigateRoomId = null)
    }

    fun generatedRoomId() = launchCatching {

        // val chatRoom = firestoreService.getChatRoom(chatId)

        val myId = accountService.currentUserId
        val otherId = uiState.value.user?.id

        val roomId = FirestoreServiceImpl.getChatRoomId(myId!!, otherId!!)

        val chatRoom = firestoreService.getChatRoomFromChatRoom(roomId)
        if (chatRoom == null) {
            android.util.Log.d("myTag", "no room, so try to create one ")
            val newChatRoom = ChatRoomFromChatRoom(
                roomId,
                arrayListOf(myId, otherId),
            )
            val result = firestoreService.saveChatRoomFromChatRoom(roomId, newChatRoom)
            android.util.Log.d("myTag", "setting chat room")

            val inputData = workDataOf(
                SaveChatRoomFromUserChatRoom.CHAT_ID to roomId,
                SaveChatRoomFromUserChatRoom.ME_ID to _meId,
                SaveChatRoomFromUserChatRoom.ME_PHOTO to mePhoto,
                SaveChatRoomFromUserChatRoom.ME_USERNAME to meUsername,
                SaveChatRoomFromUserChatRoom.PARTNER_ID to partnerId,
                SaveChatRoomFromUserChatRoom.PARTNER_PHOTO to partnerPhoto,
                SaveChatRoomFromUserChatRoom.PARTNER_USERNAME to partnerUsername
            )

            val uploadRequest = OneTimeWorkRequestBuilder<SaveChatRoomFromUserChatRoom>()
                .setInputData(inputData)
                .build()

            WorkManager.getInstance(application).enqueue(uploadRequest)

            if (result) {
                android.util.Log.d("myTag", "return run blocking with room id :$roomId")
                //return@runBlocking roomId
                uiState.value = uiState.value.copy(navigateRoomId = roomId)
            } else {
                android.util.Log.d("myTag", "return run blocking with null")
                //return@runBlocking null
            }
            //uiState.value = uiState.value.copy(chatRoom = newChatRoom)

        } else {
            android.util.Log.d("myTag", "already exists, so return immediately :$roomId")
            uiState.value = uiState.value.copy(navigateRoomId = roomId)
            //return@runBlocking roomId
            //uiState.value = uiState.value.copy(chatRoom = chatRoom)
        }


        //val chatRoom = firestoreService.getChatRoom(roomId)
        //uiState.value = uiState.value.copy(chatRoom = chatRoom)
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

    }


    var showReportDialog = mutableStateOf(false)
    var showReportDone = mutableStateOf(false)
    fun onReportClick() {
        showReportDialog.value = true
    }

    fun onReportButtonClick() {
        launchCatching {
            // firestoreService.saveReportUserMarriage(_userMarriage.value ?: UserMarriage())
            showReportDialog.value = false
            showReportDone.value = true
        }
    }

    fun onReportDoneDismiss(popUp: () -> Unit) {
        launchCatching {
            showReportDone.value = false
            popUp()
        }
    }

    fun onSoundClick(snackbarHostState: SnackbarHostState) {
        launchCatching {
            snackbarHostState.showSnackbar(
                message = "this function is not working now",
                duration = SnackbarDuration.Short,
            )
        }
    }

    private fun getUserExplore(userId: String) = launchCatching {
        val newList = mutableListOf<ExploreUserItem>()
        ff.collection(FirestoreServiceImpl.USER_COLLECTION).document(userId)
            .collection(FirestoreServiceImpl.EXPLORE_COLLECTION).get().addOnCompleteListener {
                if (it.isSuccessful && it.result != null) {
                    val x = it.result.toObjects(ExploreUserItem::class.java)

                    for (i in x) {
                        newList.add(i)
                    }
                    newList.sortByDescending { it.dateOfCreation }
                    media = newList

                }
            }
    }

    private fun getUserInfo(xy: User) {
        launchCatching {
            accountInfo.value = accountInfo.value.copy(aim = xy.type)
            when (xy.type) {
                SelectType.FLIRT -> {
                    firestoreService.getUserFlirt(country = xy.country, partnerId)?.let {
                        onAccountInfo(it.photo, it.username, it.description, it.birthday, it.gender, it.online)
                    }
                }

                SelectType.MARRIAGE -> {
                    firestoreService.getUserMarriage(country = xy.country, partnerId)?.let {
                        onAccountInfo(it.photo, it.username, it.description, it.birthday, it.gender, it.online)
                    }
                }

                SelectType.LANGUAGE_PRACTICE -> {
                    firestoreService.getUserLP(motherTongue = xy.motherTongue, partnerId)?.let {
                        onAccountInfo(it.photo, it.username, it.description, it.birthday, it.gender, it.online)
                    }
                }

                SelectType.HOTEL -> {
                    firestoreService.getHotel(country = xy.country, partnerId)?.let {
                        onAccountInfo(it.photo, it.username, it.description, "", "", it.online)
                    }
                }

                SelectType.RESTAURANT -> {
                    firestoreService.getRestaurant(country = xy.country, partnerId)?.let {
                        onAccountInfo(it.photo, it.username, it.description, "", "", it.online)
                    }
                }

                SelectType.CAFE -> {
                    firestoreService.getCafe(country = xy.country, partnerId)?.let {
                        onAccountInfo(it.photo, it.username, it.description, "", "", it.online)
                    }
                }

                else -> {}
            }
        }
    }

    private fun onAccountInfo(
        photo: String,
        username: String,
        bio: String,
        age: String,
        gender: String,
        online: Boolean
    ) {
        accountInfo.value = accountInfo.value.copy(photo = photo)
        accountInfo.value = accountInfo.value.copy(username = username)
        accountInfo.value = accountInfo.value.copy(bio = bio)
        accountInfo.value = accountInfo.value.copy(age = age)
        accountInfo.value = accountInfo.value.copy(gender = gender)
        accountInfo.value = accountInfo.value.copy(online = online)
    }
    private fun onMeInfo(
        photo: String,
        username: String,
    ) {
        accountInfo.value = accountInfo.value.copy(mePhoto = photo)
        accountInfo.value = accountInfo.value.copy(meUsername = username)
    }
}
