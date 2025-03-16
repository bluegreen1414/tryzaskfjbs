package com.kapirti.social_chat_food_video.ui.presentation.userprofile.func

import android.content.Context
import androidx.work.*
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import androidx.work.WorkerParameters
import com.google.firebase.Timestamp
import com.kapirti.social_chat_food_video.model.ChatRoomFromUserChat
import com.kapirti.social_chat_food_video.model.service.impl.FirestoreServiceImpl


class SaveChatRoomFromUserChatRoom @Inject constructor(
    @ApplicationContext private val context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {
    private val ff = FirebaseFirestore.getInstance()
    private val userCollection = ff.collection(FirestoreServiceImpl.USER_COLLECTION)


    override fun doWork(): Result {
        val chatId = inputData.getString(CHAT_ID) ?: return Result.failure()
        val meId = inputData.getString(ME_ID) ?: return Result.failure()
        val mePhoto = inputData.getString(ME_PHOTO) ?: return Result.failure()
        val meUsername = inputData.getString(ME_USERNAME) ?: return Result.failure()
        val partnerId = inputData.getString(PARTNER_ID) ?: return Result.failure()
        val partnerPhoto = inputData.getString(PARTNER_PHOTO) ?: return Result.failure()
        val partnerUsername = inputData.getString(PARTNER_USERNAME) ?: return Result.failure()

        userCollection.document(meId).collection(FirestoreServiceImpl.CHAT_COLLECTION)
            .document(partnerId).set(
                ChatRoomFromUserChat(
                    partnerPhoto = partnerPhoto,
                    partnerUsername = partnerUsername,
                    chatId = chatId,
                    lastMessageTime = Timestamp.now()
                )
            ).addOnSuccessListener {
                userCollection.document(partnerId).collection(FirestoreServiceImpl.CHAT_COLLECTION)
                    .document(meId).set(
                        ChatRoomFromUserChat(
                            partnerPhoto = mePhoto,
                            partnerUsername = meUsername,
                            chatId = chatId,
                            lastMessageTime = Timestamp.now()
                        )
                    )
            }
        return Result.success()
    }

    companion object {
        const val CHAT_ID = "chatId"
        const val ME_ID = "meId"
        const val ME_PHOTO = "mePhoto"
        const val ME_USERNAME = "meUsername"
        const val PARTNER_ID = "partnerId"
        const val PARTNER_PHOTO = "partnerPhoto"
        const val PARTNER_USERNAME = "partnerUsername"
    }
}
