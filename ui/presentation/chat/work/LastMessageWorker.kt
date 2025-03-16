package com.kapirti.social_chat_food_video.ui.presentation.chat.work

import android.content.Context
import androidx.work.*
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import androidx.work.WorkerParameters
import com.google.firebase.Timestamp
import com.kapirti.social_chat_food_video.model.ChatRoomFromChatRoom
import com.kapirti.social_chat_food_video.model.ChatRoomFromUserChat
import com.kapirti.social_chat_food_video.model.User
import com.kapirti.social_chat_food_video.model.service.impl.FirestoreServiceImpl
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class LastMessageWorker @Inject constructor(
    @ApplicationContext private val context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {
    private val ff = FirebaseFirestore.getInstance()
    private val userCollection = ff.collection(FirestoreServiceImpl.USER_COLLECTION)


    override fun doWork(): Result {
        val myId = inputData.getString(FirestoreServiceImpl.CAFE_COLLECTION) ?: return Result.failure()
        val partnerId = inputData.getString(FirestoreServiceImpl.CHAT_COLLECTION) ?: return Result.failure()
        val text = inputData.getString(FirestoreServiceImpl.EXPLORE_COLLECTION) ?: return Result.failure()

        val myItem = userCollection.document(myId).collection(FirestoreServiceImpl.CHAT_COLLECTION).document(partnerId)
        val partnerItem = userCollection.document(partnerId).collection(FirestoreServiceImpl.CHAT_COLLECTION).document(myId)
        val timestamp = Timestamp.now()

        myItem.update(FirestoreServiceImpl.LAST_MESSAGE_FIELD, text).addOnCompleteListener {
            if (it.isSuccessful) {
                myItem.update(FirestoreServiceImpl.LAST_MESSAGE_SENDER_ID_FIELD, myId).addOnCompleteListener {
                    myItem.update(FirestoreServiceImpl.LAST_MESSAGE_TIME_FIELD, timestamp).addOnCompleteListener {
                        if (it.isSuccessful) {
                            partnerItem.update(FirestoreServiceImpl.LAST_MESSAGE_FIELD, text).addOnCompleteListener {
                                if (it.isSuccessful) {
                                    partnerItem.update(FirestoreServiceImpl.LAST_MESSAGE_SENDER_ID_FIELD, myId).addOnCompleteListener {
                                        if (it.isSuccessful) {
                                            partnerItem.update(FirestoreServiceImpl.LAST_MESSAGE_TIME_FIELD, timestamp).addOnCompleteListener {
                                                if (it.isSuccessful) {

                                                    partnerItem.get().addOnSuccessListener { documentSnapshot ->
                                                        val x = documentSnapshot.toObject(ChatRoomFromUserChat::class.java)

                                                        if(x != null) {
                                                            val unreaded = x.unread
                                                            val newUnreaded = unreaded + 1
                                                            partnerItem.update(FirestoreServiceImpl.UNREAD_FIELD, newUnreaded)
                                                        }

                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return Result.success()
    }
}
