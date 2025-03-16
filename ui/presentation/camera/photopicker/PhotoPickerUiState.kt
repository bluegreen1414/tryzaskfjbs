package com.kapirti.social_chat_food_video.ui.presentation.camera.photopicker

data class PhotoPickerUiState(
    val text: String = "",
    val isDoneBtnWorking: Boolean = false,
    val isErrorText: Boolean = false,
    val isErrorPhotos: Boolean = false,
)
