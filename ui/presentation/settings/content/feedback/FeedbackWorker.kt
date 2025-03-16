package com.kapirti.social_chat_food_video.ui.presentation.settings.content.feedback

import android.content.Context
import androidx.work.*
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import androidx.work.WorkerParameters
import com.google.firebase.auth.FirebaseAuth
import com.kapirti.social_chat_food_video.model.Feedback

class FeedbackWorker @Inject constructor(
    @ApplicationContext private val context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {
    private val ff = FirebaseFirestore.getInstance()
    private val feedbackCollection = ff.collection(FEEDBACK_COLLECTION)
    private val auth = FirebaseAuth.getInstance()


    override fun doWork(): Result {
        val text = inputData.getString(FEEDBACK_COLLECTION) ?: return Result.failure()

        feedbackCollection.add(Feedback(
            text = text,
            uid = auth.currentUser?.uid ?: "",
            email = auth.currentUser?.email ?: ""
        ))

        return Result.success()
    }

    companion object {
        const val FEEDBACK_COLLECTION = "Feedback"
    }
}
