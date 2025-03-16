package com.kapirti.social_chat_food_video.ui.presentation.chat

import com.kapirti.social_chat_food_video.model.ChatRoomFromChatRoom
import com.kapirti.social_chat_food_video.model.GetChatMessage

data class ChatUiState(
    val chatRoom: ChatRoomFromChatRoom? = null,
    val messages: List<GetChatMessage> = listOf(),

    val partnerId: String = "partnerId",
    val partnerPhoto: String = "partnerPhoto",
    val partnerUserName: String = "partnerUserName",
 //
   // val otherUserName: String = "",
    //val otherUserChatState: String = ""
)
