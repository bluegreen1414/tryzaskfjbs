package com.kapirti.social_chat_food_video.ui.presentation.settings.content.deleteaccount

data class DeleteUiState(
    val password: String = "",
    val text: String = "",
    val isErrorPassword: Boolean = false,
    val isDoneBtnWorking: Boolean = false,
)
