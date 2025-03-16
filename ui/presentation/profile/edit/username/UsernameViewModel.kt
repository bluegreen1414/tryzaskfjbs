package com.kapirti.social_chat_food_video.ui.presentation.profile.edit.username

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.kapirti.social_chat_food_video.model.service.AccountService
import com.kapirti.social_chat_food_video.model.service.LogService
import com.kapirti.social_chat_food_video.model.service.impl.FirestoreServiceImpl
import com.kapirti.social_chat_food_video.ui.presentation.KapirtiViewModel
import com.kapirti.social_chat_food_video.ui.presentation.settings.content.feedback.FeedbackUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class UsernameViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val accountService: AccountService,
    logService: LogService
): KapirtiViewModel(logService){
    var uiState = mutableStateOf(FeedbackUiState())
        private set


    private val text
        get() = uiState.value.text


    fun saveUsername(popUp: () -> Unit) {
        launchCatching {
            onIsDoneBtnWorking(true)
            if (text.isBlank()) {
                popUp()
                return@launchCatching
            }

            val uid = accountService.currentUserId
            val inputData = workDataOf(
                FirestoreServiceImpl.DESCRIPTION_FIELD to uid,
                FirestoreServiceImpl.EXPLORE_COLLECTION to text
            )

            val uploadRequest = OneTimeWorkRequestBuilder<UsernameWorker>()
                .setInputData(inputData)
                .build()

            WorkManager.getInstance(context).enqueue(uploadRequest)

            popUp()
        }
    }

    private fun onIsDoneBtnWorking(newValue: Boolean) { uiState.value = uiState.value.copy(isDoneBtnWorking = newValue) }
    fun onTextChange(newValue: String) { uiState.value = uiState.value.copy(text = newValue) }
}
