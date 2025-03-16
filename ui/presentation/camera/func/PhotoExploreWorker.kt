package com.kapirti.social_chat_food_video.ui.presentation.camera.func

import android.content.Context
import androidx.work.*
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import androidx.work.WorkerParameters
import com.google.firebase.Timestamp
import com.kapirti.social_chat_food_video.model.ExplorePhotoVideo
import com.kapirti.social_chat_food_video.model.ExploreType
import com.kapirti.social_chat_food_video.model.ExploreUserItem
import com.kapirti.social_chat_food_video.model.service.impl.FirestoreServiceImpl

class PhotoExploreWorker @Inject constructor(
    @ApplicationContext private val context: Context,
    workerParams: WorkerParameters,
) : Worker(context, workerParams) {
    private val ff = FirebaseFirestore.getInstance()
    private val explorePhotoCollection = ff.collection(FirestoreServiceImpl.EXPLORE_PHOTO_COLLECTION)
    private val userCollection = ff.collection(FirestoreServiceImpl.USER_COLLECTION)

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


        val item = ExplorePhotoVideo(
            uri = link,
            dateOfCreation = Timestamp.now(),
            description = description,
            userId = uid,
            username = username,
            userPhoto = userPhoto,
        )

        explorePhotoCollection.add(item).addOnSuccessListener { its ->
            val x = its.id

            val userItem = ExploreUserItem(
                type = ExploreType.PHOTO.toString(),
                uri = link,
                dateOfCreation = Timestamp.now(),
                description = description,
            )


            userCollection.document(uid).collection(FirestoreServiceImpl.EXPLORE_COLLECTION)
                .document(x)
                .set(userItem)

        }
        return Result.success()
    }
}
