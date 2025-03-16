package com.kapirti.social_chat_food_video.ui.presentation.camera.func

import android.content.Context
import androidx.work.*
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import androidx.work.WorkerParameters
import com.google.firebase.Timestamp
import com.kapirti.social_chat_food_video.model.ExploreType
import com.kapirti.social_chat_food_video.model.service.impl.FirestoreServiceImpl
import com.kapirti.social_chat_food_video.ui.presentation.story.Story
import com.kapirti.social_chat_food_video.ui.presentation.story.StoryItem

class PhotoStoryWorker @Inject constructor(
    @ApplicationContext private val context: Context,
    workerParams: WorkerParameters,
) : Worker(context, workerParams) {
    private val ff = FirebaseFirestore.getInstance()
    private val storyCollection = ff.collection(FirestoreServiceImpl.STORY_COLLECTION)

    override fun doWork(): Result {
        val uid =
            inputData.getString(FirestoreServiceImpl.ADDRESS_FIELD) ?: return Result.failure()
        val link =
            inputData.getString(FirestoreServiceImpl.EXPLORE_COLLECTION) ?: return Result.failure()
        val userPhoto =
            inputData.getString(FirestoreServiceImpl.PHOTO_FIELD) ?: return Result.failure()
        val username =
            inputData.getString(FirestoreServiceImpl.USERNAME_FIELD) ?: return Result.failure()
        val description =
            inputData.getString(FirestoreServiceImpl.DESCRIPTION_FIELD) ?: return Result.failure()


        val item = Story(
            dateOfCreation = Timestamp.now(),
            username = username,
            photo = userPhoto,
        )
        storyCollection.document(uid).set(item).addOnCompleteListener {
            if (it.isSuccessful) {
                val storyItem = StoryItem(
                    type = ExploreType.PHOTO.toString(),
                    uri = link,
                    description = description,
                    dateOfCreation = Timestamp.now(),
                )

                storyCollection.document(uid)
                    .collection(FirestoreServiceImpl.STORY_COLLECTION)
                    .add(storyItem)
            }
        }
        return Result.success()
    }
}
