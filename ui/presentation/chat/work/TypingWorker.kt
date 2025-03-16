package com.kapirti.social_chat_food_video.ui.presentation.chat.work

import android.content.Context
import androidx.work.*
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import androidx.work.WorkerParameters
import com.kapirti.social_chat_food_video.model.service.impl.FirestoreServiceImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TypingWorker @Inject constructor(
    @ApplicationContext private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {
    private val ff = FirebaseFirestore.getInstance()
    private val userCollection = ff.collection(FirestoreServiceImpl.USER_COLLECTION)


    override suspend fun doWork(): Result {
        val myId = inputData.getString(FirestoreServiceImpl.CAFE_COLLECTION) ?: return Result.failure()
        val partnerId = inputData.getString(FirestoreServiceImpl.CHAT_COLLECTION) ?: return Result.failure()

        userCollection.document(partnerId).collection(FirestoreServiceImpl.CHAT_COLLECTION).document(myId)
            .update(FirestoreServiceImpl.TYPING_FIELD, true).addOnCompleteListener {
                if (it.isSuccessful) {

                    CoroutineScope(Dispatchers.IO).launch {
                        delay(10000L)
                        userCollection.document(partnerId)
                            .collection(FirestoreServiceImpl.CHAT_COLLECTION).document(myId)
                            .update(FirestoreServiceImpl.TYPING_FIELD, false)
                    }
                }
            }

        return Result.success()
    }
}
