package com.kapirti.social_chat_food_video.ui.presentation.profile.edit.username

import android.content.Context
import androidx.work.*
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import androidx.work.WorkerParameters
import com.kapirti.social_chat_food_video.core.constants.SelectType
import com.kapirti.social_chat_food_video.model.service.impl.FirestoreServiceImpl

class UsernameWorker @Inject constructor(
    @ApplicationContext private val context: Context,
    workerParams: WorkerParameters,
) : Worker(context, workerParams) {
    private val ff = FirebaseFirestore.getInstance()
    private val userCollection = ff.collection(FirestoreServiceImpl.USER_COLLECTION)
    private val userFlirtCollection = ff.collection(FirestoreServiceImpl.FLIRT_COLLECTION)
    private val userMarriageCollection = ff.collection(FirestoreServiceImpl.MARRIAGE_COLLECTION)
    private val userLanguagePracticeCollection = ff.collection(FirestoreServiceImpl.LANGUAGE_PRACTICE_COLLECTION)
    private val hotelCollection = ff.collection(FirestoreServiceImpl.HOTEL_COLLECTION)
    private val restaurantCollection = ff.collection(FirestoreServiceImpl.RESTAURANT_COLLECTION)
    private val cafeCollection = ff.collection(FirestoreServiceImpl.CAFE_COLLECTION)


    override fun doWork(): Result {
        val uid = inputData.getString(FirestoreServiceImpl.DESCRIPTION_FIELD) ?: return Result.failure()
        val text = inputData.getString(FirestoreServiceImpl.EXPLORE_COLLECTION) ?: return Result.failure()


        userCollection.document(uid).get().addOnSuccessListener {
            if (it != null && it.exists()) {
                val type = it.get(FirestoreServiceImpl.TYPE_FIELD)
                val country = it.get(FirestoreServiceImpl.COUNTRY_FIELD).toString()
                when (type) {
                    SelectType.FLIRT -> {
                        userFlirtCollection.document(country).collection(country).document(uid)
                            .update(FirestoreServiceImpl.USERNAME_FIELD, text) }

                    SelectType.MARRIAGE -> {
                        userMarriageCollection.document(country).collection(country).document(uid)
                            .update(FirestoreServiceImpl.USERNAME_FIELD, text) }

                    SelectType.LANGUAGE_PRACTICE -> {
                        val motherTongue = it.get(FirestoreServiceImpl.MOTHER_TONGUE_FIELD).toString()
                        userLanguagePracticeCollection.document(motherTongue).collection(motherTongue).document(uid)
                            .update(FirestoreServiceImpl.USERNAME_FIELD, text) }

                    SelectType.HOTEL -> {
                        hotelCollection.document(country).collection(FirestoreServiceImpl.HOTEL_COLLECTION).document(uid)
                            .update(FirestoreServiceImpl.USERNAME_FIELD, text) }

                    SelectType.RESTAURANT -> {
                        restaurantCollection.document(country).collection(FirestoreServiceImpl.RESTAURANT_COLLECTION).document(uid)
                            .update(FirestoreServiceImpl.USERNAME_FIELD, text) }

                    SelectType.CAFE -> {
                        cafeCollection.document(country).collection(FirestoreServiceImpl.CAFE_COLLECTION).document(uid)
                            .update(FirestoreServiceImpl.USERNAME_FIELD, text) }

                    else -> return@addOnSuccessListener
                }
            }
        }
        return Result.success()
    }
}

