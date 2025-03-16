package com.kapirti.social_chat_food_video.ui.presentation.edit.func

import android.content.Context
import androidx.core.net.toUri
import androidx.work.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import androidx.work.WorkerParameters
import com.google.firebase.Timestamp
import com.kapirti.social_chat_food_video.core.constants.SelectType
import com.kapirti.social_chat_food_video.core.constants.getCountryLanguage
import com.kapirti.social_chat_food_video.model.Cafe
import com.kapirti.social_chat_food_video.model.ExplorePhotoVideo
import com.kapirti.social_chat_food_video.model.service.impl.FirestoreServiceImpl
import com.kapirti.social_chat_food_video.model.service.impl.StorageServiceImpl


class CafeWorker @Inject constructor(
    @ApplicationContext private val context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {
    private val ff = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    private val userCollection = ff.collection(FirestoreServiceImpl.USER_COLLECTION)
    private val cafeCollection = ff.collection(FirestoreServiceImpl.CAFE_COLLECTION)
    private val explorePhotoCollection = ff.collection(FirestoreServiceImpl.EXPLORE_PHOTO_COLLECTION)


    override fun doWork(): Result {
        val randomid = inputData.getString(FirestoreServiceImpl.DESCRIPTION_FIELD) ?: return Result.failure()
        val uid = inputData.getString(FirestoreServiceImpl.HOTEL_COLLECTION) ?: return Result.failure()
        val photoFile = inputData.getString(FirestoreServiceImpl.RESTAURANT_COLLECTION) ?: return Result.failure()
        val country = inputData.getString(FirestoreServiceImpl.COUNTRY_FIELD) ?: return Result.failure()
        val username = inputData.getString(FirestoreServiceImpl.USERNAME_FIELD) ?: return Result.failure()
        val city = inputData.getString(FirestoreServiceImpl.CITY_FIELD) ?: return Result.failure()
        val address = inputData.getString(FirestoreServiceImpl.ADDRESS_FIELD) ?: return Result.failure()
        val description = inputData.getString(FirestoreServiceImpl.DESCRIPTION_FIELD) ?: return Result.failure()



        val vr = storage.reference.child(StorageServiceImpl.PHOTOS_STORAGE).child(uid).child("${randomid.toUri()}.jpg")
        vr.putFile(photoFile.toUri()).addOnCompleteListener {
            if (!it.isSuccessful) {
                return@addOnCompleteListener
            } else {
                vr.downloadUrl.addOnSuccessListener {
                    val photoDownload = it.toString()

                    userCollection.document(uid).update(FirestoreServiceImpl.TYPE_FIELD, SelectType.CAFE).addOnSuccessListener {
                        userCollection.document(uid).update(FirestoreServiceImpl.LEARN_LANGUAGE_FIELD, getCountryLanguage(country)).addOnSuccessListener {
                            userCollection.document(uid).update(FirestoreServiceImpl.COUNTRY_FIELD, country).addOnSuccessListener {
                                cafeCollection.document(country).collection(FirestoreServiceImpl.CAFE_COLLECTION).document(uid).set(
                                    Cafe(
                                        displayName = username,
                                        photo = photoDownload,
                                        username = username,
                                        city = city,
                                        address = address,
                                        description = description,
                                        country = country,
                                        online = true,
                                        dateOfCreation = Timestamp.now(),
                                    )
                                ).addOnSuccessListener {

                                    val item = ExplorePhotoVideo(
                                        uri = photoDownload,
                                        dateOfCreation = Timestamp.now(),
                                        description = description,
                                        userId = uid,
                                        username = username,
                                        userPhoto = photoDownload,
                                    )

                                    explorePhotoCollection.add(item)
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
