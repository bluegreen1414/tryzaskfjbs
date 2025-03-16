package com.kapirti.social_chat_food_video.model

import androidx.annotation.StringRes
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import com.kapirti.social_chat_food_video.R.string as AppText

enum class Theme {
    Light, Dark
}

data class Feedback (
    val text: String = "",
    val uid: String = "",
    val email: String = "",
)

data class Recent(
    @DocumentId val id: String = "",
    val displayName: String = "",
    val photo: String = "",
    @ServerTimestamp
    val dateOfCreation: Timestamp? = null
)

data class CountryFlag(
    val country: String = "",
    @StringRes val text: Int = AppText.cc_united_states,
    val flag: String = ""
)
