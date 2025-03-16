package com.kapirti.social_chat_food_video.ui.presentation.camera

import android.net.Uri

data class Media(var uri: Uri, var mediaType: MediaType)

enum class MediaType {
    PHOTO,
    VIDEO,
}
