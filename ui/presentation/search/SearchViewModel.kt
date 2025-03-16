package com.kapirti.social_chat_food_video.ui.presentation.search

import com.kapirti.social_chat_food_video.common.stateInUi
import com.kapirti.social_chat_food_video.model.service.FirestoreService
import com.kapirti.social_chat_food_video.model.service.LogService
import com.kapirti.social_chat_food_video.ui.presentation.KapirtiViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val firestoreService: FirestoreService,
    logService: LogService
): KapirtiViewModel(logService){
    val nicknames = firestoreService.nicknames.stateInUi(emptyList())
/**
    fun onSearchClick(user: User, navigateAndPopUpSearchToUserProfile: () -> Unit){
        launchCatching {
            firestoreService.saveUserRecentUser(uid = user.id,
                Recent(
                    displayName = user.displayName,
                    photo = user.photo,
                    dateOfCreation = Timestamp.now()
                )
            )
            navigateAndPopUpSearchToUserProfile()
        }
    }
    fun onDeleteClick(recent: Recent){
        launchCatching {
            firestoreService.deleteUserRecentUser(recent.id)
        }
    }*/
}
