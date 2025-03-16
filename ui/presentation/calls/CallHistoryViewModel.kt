package com.kapirti.social_chat_food_video.ui.presentation.calls

import com.kapirti.social_chat_food_video.common.stateInUi
import com.kapirti.social_chat_food_video.model.service.FirestoreService
import com.kapirti.social_chat_food_video.model.service.LogService
import com.kapirti.social_chat_food_video.ui.presentation.KapirtiViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CallHistoryViewModel @Inject constructor(
    private val firestoreService: FirestoreService,
    logService: LogService
) : KapirtiViewModel(logService) {
    val calls = firestoreService.callRecords.stateInUi(emptyList())
}
