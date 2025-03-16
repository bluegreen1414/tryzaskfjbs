package com.kapirti.social_chat_food_video.ui.presentation.camera.func

import android.content.Context
import androidx.core.net.toUri
import androidx.work.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import androidx.work.WorkerParameters
import com.google.firebase.Timestamp
import com.kapirti.social_chat_food_video.model.ExploreType
import com.kapirti.social_chat_food_video.model.SaveChatMessage
import com.kapirti.social_chat_food_video.model.service.impl.FirestoreServiceImpl
import com.kapirti.social_chat_food_video.model.service.impl.StorageServiceImpl

class VideoChatWorker @Inject constructor(
    @ApplicationContext private val context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {
    private val ff = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val chatRoomCollection = ff.collection(FirestoreServiceImpl.CHATROOM_COLLECTION)

    override fun doWork(): Result {
        val randomUid =
            inputData.getString(FirestoreServiceImpl.CHAT_COLLECTION) ?: return Result.failure()
        val uid =
            inputData.getString(FirestoreServiceImpl.CAFE_COLLECTION) ?: return Result.failure()
        val videoFile =
            inputData.getString(FirestoreServiceImpl.EXPLORE_COLLECTION) ?: return Result.failure()
        val chatId =
            inputData.getString(FirestoreServiceImpl.USERNAME_FIELD) ?: return Result.failure()

        val vr = storage.reference.child(StorageServiceImpl.PHOTOS_STORAGE).child(uid).child("${randomUid}.mp4")
        vr.putFile(videoFile.toUri()).addOnCompleteListener {
            if (!it.isSuccessful) {
                return@addOnCompleteListener
            } else {
                vr.downloadUrl.addOnSuccessListener {
                    val videoDownload = it.toString()

                    val item = SaveChatMessage(
                        senderId = uid,
                        mediaUri = videoDownload,
                        mediaMimeType = ExploreType.VIDEO.toString(),
                        timestamp = Timestamp.now()
                    )
                    chatRoomCollection.document(chatId).collection("chats")
                        .add(item).addOnSuccessListener {}
                }
            }
        }
        return Result.success()
    }
}
