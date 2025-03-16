package com.kapirti.social_chat_food_video.ui.presentation.notification

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.google.android.samples.socialite.ui.Bubble

class BubbleActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val chatId = intent.data?.lastPathSegment?.toLongOrNull()
        setContent {
            Bubble(chatId = chatId!!)
        }
    }
}
