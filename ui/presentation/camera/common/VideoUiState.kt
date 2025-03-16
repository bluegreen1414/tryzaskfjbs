package com.kapirti.social_chat_food_video.ui.presentation.camera.common

import android.net.Uri

data class VideoUiState(
    val uri: Uri? = null,
    val isDoneBtnWorking: Boolean = false,
)
