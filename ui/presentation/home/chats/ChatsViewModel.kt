package com.kapirti.social_chat_food_video.ui.presentation.home.chats

import com.kapirti.social_chat_food_video.common.stateInUi
import com.kapirti.social_chat_food_video.model.service.AccountService
import com.kapirti.social_chat_food_video.model.service.FirestoreService
import com.kapirti.social_chat_food_video.model.service.LogService
import com.kapirti.social_chat_food_video.ui.presentation.KapirtiViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatsViewModel @Inject constructor(
    //repository: ChatRepository,
    private val accountService: AccountService,
    private val firestoreService: FirestoreService,
    logService: LogService
) : KapirtiViewModel(logService) {
    val meId = accountService.currentUserId

    val chats = firestoreService.userChats
        .stateInUi(emptyList())
}
/**
    var uiState = mutableStateOf(ChatsUiState())
        private set

    init {
        observeChatRooms()
    }

}

    @OptIn(ExperimentalCoroutinesApi::class)
//    val conversations = firestoreService.currentUserConversations.stateInUi(emptyList()).map {
//        it.map { mapChatItem(it) } //mapChatItem(it)
//    }






    //val chats = listOf<ChatDetail>()

    suspend fun mapChatItem(room: ChatRoom) : ChatRow {
        val myId = firebaseAuth.currentUser?.uid.orEmpty()
        val otherId = room.userIds.filterNot { it == myId }.first()
        val otherUser = firestoreService.getUser(otherId)!!
        val lastMessageSentByMe: Boolean = room.lastMessageSenderId == myId
        return ChatRow(
            name = otherUser.name,
            lastMessage = (if (lastMessageSentByMe) "You : " else "") + room.lastMessage,
            profileImage = "",
            lastTime = (room.lastMessageTime?.seconds?: 0L) * 1000L,
            userIds = room.userIds
        )
    }

//    val chats = repository
//        .getChats()
//        .stateInUi(emptyList())


    fun observeChatRooms() = viewModelScope.launch {
        val myId = firebaseAuth.currentUser?.uid.orEmpty()
        android.util.Log.d("myTag","start observing with my id : $myId ")
        firestoreService.getConversations(myId).collectLatest { rooms ->
            val rows = rooms.map { room ->
                val otherId = room.userIds.filterNot { it == myId }.first()
                val otherUser = firestoreService.getUser(otherId)!!
                val lastMessageSentByMe: Boolean = room.lastMessageSenderId == myId
                ChatRow(
                    roomId = room.id,
                    name = otherUser.name,
                    lastMessage = (if (lastMessageSentByMe) "You : " else "") + room.lastMessage,
                    profileImage = "",
                    lastTime = (room.lastMessageTime?.seconds?: 0L) * 1000L,
                    userIds = room.userIds
                )
            }
            uiState.value = uiState.value.copy(conversationList = rows)

            //uiState.value = uiState.value.copy(messages = it.map { com.test.test.ui.presentation.chats.ChatMessage(text = it.message, isIncoming = it.senderId != myId) })
        }
    }

}
*/
