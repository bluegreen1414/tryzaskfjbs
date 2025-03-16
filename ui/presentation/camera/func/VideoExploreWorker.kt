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
import com.kapirti.social_chat_food_video.model.service.impl.FirestoreServiceImpl
import com.kapirti.social_chat_food_video.model.service.impl.StorageServiceImpl
import com.kapirti.social_chat_food_video.model.ExplorePhotoVideo
import com.kapirti.social_chat_food_video.model.ExploreType
import com.kapirti.social_chat_food_video.model.ExploreUserItem

class VideoExploreWorker @Inject constructor(
    @ApplicationContext private val context: Context,
    workerParams: WorkerParameters,
) : Worker(context, workerParams) {
    private val ff = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val exploreVideoCollection = ff.collection(FirestoreServiceImpl.EXPLORE_COLLECTION)
    private val userCollection = ff.collection(FirestoreServiceImpl.USER_COLLECTION)

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


        val vr = storage.reference.child(StorageServiceImpl.PHOTOS_STORAGE).child(uid).child("${randomUid}.mp4")
        vr.putFile(videoFile.toUri()).addOnCompleteListener {
            if (!it.isSuccessful) {
                return@addOnCompleteListener
            } else {
                vr.downloadUrl.addOnSuccessListener {
                    val videoDownload = it.toString()


                    val item = ExplorePhotoVideo(
                        uri = videoDownload,
                        dateOfCreation = Timestamp.now(),
                        description = description,
                        userId = uid,
                        username = username,
                        userPhoto = userPhoto,
                    )


                    exploreVideoCollection.add(item).addOnSuccessListener { its ->
                        val x = its.id

                        val userItem = ExploreUserItem(
                            type = ExploreType.VIDEO.toString(),
                            uri = videoDownload,
                            dateOfCreation = Timestamp.now(),
                            description = description,
                        )


                        userCollection.document(uid).collection(FirestoreServiceImpl.EXPLORE_COLLECTION)
                            .document(x)
                            .set(userItem)

                    }

                }
            }
        }

        return Result.success()
    }
}
