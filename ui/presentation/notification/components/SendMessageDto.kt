package com.kapirti.social_chat_food_video.ui.presentation.notification.components

data class SendMessageDto(
    val from: String?,
    val to: String?,
    val notification: NotificationBody
)

data class NotificationBody(
    val title: String,
    val body: String,
    val mediaUri: String? = null,
    val mediaMimeType: String? = null,
)
