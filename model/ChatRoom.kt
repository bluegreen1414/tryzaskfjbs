
package com.kapirti.social_chat_food_video.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp


data class ChatRoomFromChatRoom(
    @DocumentId val id: String = "id",
    val userIds: List<String> = listOf(),
    @ServerTimestamp var lastMessageTime: Timestamp? = null,
    var lastMessageSenderId: String = "",
    var lastMessage: String = "",
    var mediaUri: String? = null,
    var mediaMimeType: String? = null,
)

/**data class ChatRoom(

    //val chatStates: List<String> = listOf(),
    @ServerTimestamp
    var lastMessageTime: Timestamp? = null,
    var lastMessageSenderId: String = "",
    var lastMessage: String = "",
    var mediaUri: String? = null,
    var mediaMimeType: String? = null,
)*/

data class ChatRoomFromUserChat(
    @DocumentId val partnerId: String = "",
    val partnerPhoto: String = "",
    val partnerUsername: String = "",
    val chatId: String = "",
    var lastMessage: String = "",
    var lastMessageSenderId: String = "",
    val isTyping: Boolean = false,
    val unread: Int = 0,
    @ServerTimestamp var lastMessageTime: Timestamp? = null,
)

//var mediaUri: String? = null,
//var mediaMimeType: String? = null,
