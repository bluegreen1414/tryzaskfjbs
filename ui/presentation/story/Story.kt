package com.kapirti.social_chat_food_video.ui.presentation.story

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp

data class Story(
    @DocumentId val userId: String = "",
    val username: String = "",
    val photo: String = "",
    @ServerTimestamp var dateOfCreation: Timestamp? = null
)

data class StoryItem(
    @DocumentId val id: String = "id",
    val type: String = "",
    val uri: String = "",
    val description: String = "",
    @ServerTimestamp var dateOfCreation: Timestamp? = null
)
