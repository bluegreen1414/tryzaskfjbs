package com.kapirti.social_chat_food_video.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import com.kapirti.social_chat_food_video.ui.presentation.calls.CallType

data class CallRecord(
    //@DocumentId val id: String = "id",
    val callType: CallType? = null,
    val userId: String = "",
    //val roomId: String = "",
    val peerName: String = "",
    @ServerTimestamp var callStart: Timestamp? = null,
    @ServerTimestamp var callEnd: Timestamp? = null,
)
/**
data class UserCall(
    @DocumentId val id: String = "id",
    val photo: String = "Photo",
    val displayName: String = "Display name",
    @ServerTimestamp var dateOfCreation: Timestamp? = null
)
*/
