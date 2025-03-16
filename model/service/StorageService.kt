package com.kapirti.social_chat_food_video.model.service

import android.net.Uri

interface StorageService {
    suspend fun getPhoto(uid: String): String
    suspend fun getVideo(uid: String): String
    suspend fun savePhoto(photo: ByteArray, uid: String)
    suspend fun saveVideo(uri: Uri, uid: String)
    // suspend fun getProfile(): String
    // suspend fun saveBytes(photo: ByteArray)
}
