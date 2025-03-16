package com.kapirti.social_chat_food_video.ui.presentation.profile.edit.nickname

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object NicknameHelper {
    private val db = FirebaseFirestore.getInstance()

    suspend fun isNicknameAvailable(nickname: String): Boolean {
        val snapshot = db.collection("nicknames")
            .document(nickname)
            .get()
            .await()
        return !snapshot.exists()
    }

    suspend fun suggestNicknames(baseNickname: String, suggestionCount: Int = 5): List<String> {
        val suggestions = mutableListOf<String>()
        repeat(suggestionCount) {
            val suggestion = "$baseNickname${(100..999).random()}"
            if (isNicknameAvailable(suggestion)) {
                suggestions.add(suggestion)
            }
        }
        return suggestions
    }

    suspend fun saveNickname(nickname: String, uid: String): Boolean {
        return try {
            db.collection("nicknames")
                .document(nickname)
                .set(mapOf("uid" to uid))
                .await()
            true
        } catch (e: Exception) {
            false
        }
    }
}
