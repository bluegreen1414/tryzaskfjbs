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
import com.kapirti.social_chat_food_video.model.service.impl.FirestoreServiceImpl
import com.kapirti.social_chat_food_video.model.service.impl.StorageServiceImpl
import com.kapirti.social_chat_food_video.ui.presentation.story.Story
import com.kapirti.social_chat_food_video.ui.presentation.story.StoryItem

class VideoStoryWorker @Inject constructor(
    @ApplicationContext private val context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {
    private val ff = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val storyCollection = ff.collection(FirestoreServiceImpl.STORY_COLLECTION)


    override fun doWork(): Result {
        val randomUid =
            inputData.getString(FirestoreServiceImpl.CHAT_COLLECTION) ?: return Result.failure()
        val uid =
            inputData.getString(FirestoreServiceImpl.CAFE_COLLECTION) ?: return Result.failure()
        val videoFile =
            inputData.getString(FirestoreServiceImpl.EXPLORE_COLLECTION) ?: return Result.failure()
        val username =
            inputData.getString(FirestoreServiceImpl.USERNAME_FIELD) ?: return Result.failure()
        val userPhoto =
            inputData.getString(FirestoreServiceImpl.PHOTO_FIELD) ?: return Result.failure()
        val description =
            inputData.getString(FirestoreServiceImpl.DESCRIPTION_FIELD) ?: return Result.failure()


        val vr = storage.reference.child(StorageServiceImpl.PHOTOS_STORAGE).child(uid).child("${randomUid.toUri()}.mp4")
        vr.putFile(videoFile.toUri()).addOnCompleteListener {
            if (!it.isSuccessful) {
                return@addOnCompleteListener
            } else {
                vr.downloadUrl.addOnSuccessListener {
                    val videoDownload = it.toString()

                    val item = Story(
                        dateOfCreation = Timestamp.now(),
                        username = username,
                        photo = userPhoto,
                    )

                    storyCollection.document(uid).set(item).addOnCompleteListener {
                        if (it.isSuccessful) {
                            val storyItem = StoryItem(
                                type = ExploreType.VIDEO.toString(),
                                uri = videoDownload,
                                description = description,
                                dateOfCreation = Timestamp.now(),
                            )

                            storyCollection.document(uid)
                                .collection(FirestoreServiceImpl.STORY_COLLECTION)
                                .add(storyItem)
                        }
                    }
                }
            }
        }
        return Result.success()
    }
}

