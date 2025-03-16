
package com.kapirti.social_chat_food_video.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp

data class SaveChatMessage(
    @DocumentId val id: String = "id",
    val senderId: String = "",
    val text: String = "",
    val mediaUri: String = "",
    val mediaMimeType: String = "",
    @ServerTimestamp var timestamp: Timestamp? = null,
)


data class GetChatMessage(
    @DocumentId val id: String = "id",
    val senderId: String = "",
    val text: String = "",
    val mediaUri: String = "",
    val mediaMimeType: String = "",
    @ServerTimestamp var timestamp: Timestamp? = null,
    val isIncoming: Boolean = false,
)



 //   val type: String = "",
 //   val uri: String = "",
//    val mediaUriPhoto: String = "mediaUriPhoto",
//    val mediaUriVideo: String = "mediaUriVideo",

/**
    val text: String = "",
    //val mediaUri: String = "",
    //val mediaMimeType: String = "",
    val senderId: String = "",
    @ServerTimestamp
    var timestamp: Timestamp? = null
)


data class ChatMessageSavePhoto(
    @DocumentId val id: String = "id",
    val mediaUriPhoto: String = "",
    val senderId: String = "",
    @ServerTimestamp var timestamp: Timestamp? = null
)

*/



/**
data class ChatMessage(
    val message: String = "",
    val mediaUri: String = "",
    val mediaMimeType: String = "",
)
*/
//
 //   val senderIconUri: Uri? = null,
/**
data class ChatMessage(

    val isIncoming: Boolean = false,
    val senderIconUri: Uri? = null,
)
*/
