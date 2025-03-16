package com.kapirti.social_chat_food_video.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp

data class ExploreUserItem(
    @DocumentId val id: String = "id",
    val type: String = "",
    val uri: String = "",
    val description: String = "",
    @ServerTimestamp var dateOfCreation: Timestamp? = null
)

enum class ExploreType {
    PHOTO,
    VIDEO,
}


data class ExplorePhotoVideo(
    @DocumentId val id: String = "id",
    val uri: String = "",
    val description: String = "",
    val userId: String = "",
    val username: String = "",
    val userPhoto: String = "",
    @ServerTimestamp var dateOfCreation: Timestamp? = null
)



/**



enum class ExploreMediaType {
    PHOTO,
    VIDEO,
}
*/
/**
private val video: String,
val userImage: String,
val userName: String,
val isLiked: Boolean = false,
val likesCount: Int,
val comment: String,
val commentsCount: Int
*/
