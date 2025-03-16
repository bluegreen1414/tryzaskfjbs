package com.kapirti.social_chat_food_video.ui.presentation.userprofile

import com.kapirti.social_chat_food_video.model.User

data class UserProfileUiState(
    val user: User? = null,
    val navigateRoomId: String? = null,
)


data class UserProfileAccountUiState(
    val userId: String = "",
    val photo: String = "",
    val username: String = "",
    val bio: String = "",
    val age: String = "",
    val gender: String = "",
    val aim: String = "",
    val online: Boolean = false,
    val mePhoto: String = "",
    val meUsername: String = "",
)
