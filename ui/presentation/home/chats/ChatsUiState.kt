package com.kapirti.social_chat_food_video.ui.presentation.home.chats

import com.kapirti.social_chat_food_video.model.ChatRoomFromUserChat

data class ChatsUiState(
    //val chatRoom: ChatRoom? = null,
    //val conversationList: List<ChatRoom> = listOf()
    val conversationList: List<ChatRoomFromUserChat> = listOf()
)

//data class ChatUiState(
 //   val chatRoom: ChatRoom? = null,
   // val messages: List<ChatMessage> = listOf(),
   // val otherUserName: String = "",
    //val otherUserChatState: String = ""
//)
