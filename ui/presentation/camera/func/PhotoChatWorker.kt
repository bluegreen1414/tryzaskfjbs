package com.kapirti.social_chat_food_video.ui.presentation.camera.func

import android.content.Context
import androidx.work.*
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import androidx.work.WorkerParameters
import com.google.firebase.Timestamp
import com.kapirti.social_chat_food_video.model.ExploreType
import com.kapirti.social_chat_food_video.model.SaveChatMessage
import com.kapirti.social_chat_food_video.model.service.impl.FirestoreServiceImpl

class PhotoChatWorker @Inject constructor(
    @ApplicationContext private val context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {
    private val ff = FirebaseFirestore.getInstance()
    private val chatRoomCollection = ff.collection(FirestoreServiceImpl.CHATROOM_COLLECTION)

    override fun doWork(): Result {
        val uid =
            inputData.getString(FirestoreServiceImpl.CAFE_COLLECTION) ?: return Result.failure()
        val videoFile =
            inputData.getString(FirestoreServiceImpl.EXPLORE_COLLECTION) ?: return Result.failure()
        val chatId =
            inputData.getString(FirestoreServiceImpl.USERNAME_FIELD) ?: return Result.failure()


        val item = SaveChatMessage(
            senderId = uid,
            mediaUri = videoFile,
            mediaMimeType = ExploreType.PHOTO.toString(),
            timestamp = Timestamp.now()
        )
        chatRoomCollection.document(chatId).collection("chats").add(item).addOnSuccessListener {}
        return Result.success()
    }
}
