package com.kapirti.social_chat_food_video.ui.presentation.profile.edit.nickname

import com.google.firebase.firestore.DocumentId

data class Nickname(
    @DocumentId val nickname: String = "nickname",
    val id: String = "id",
)
