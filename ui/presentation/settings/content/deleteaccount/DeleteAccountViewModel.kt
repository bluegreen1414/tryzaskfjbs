package com.kapirti.social_chat_food_video.ui.presentation.settings.content.deleteaccount

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.kapirti.social_chat_food_video.model.Delete
import com.kapirti.social_chat_food_video.model.service.AccountService
import com.kapirti.social_chat_food_video.model.service.FirestoreService
import com.kapirti.social_chat_food_video.model.service.LogService
import com.kapirti.social_chat_food_video.model.service.impl.FirestoreServiceImpl
import com.kapirti.social_chat_food_video.ui.presentation.KapirtiViewModel
import com.kapirti.social_chat_food_video.ui.presentation.main.LastSeenWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class DeleteAccountViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val firestoreService: FirestoreService,
    private val accountService: AccountService,
    logService: LogService
): KapirtiViewModel(logService){
    var uiState = mutableStateOf(DeleteUiState())
        private set


    private val password
        get() = uiState.value.password


    private val text
        get() = uiState.value.text


    fun onDeleteMyAccountClick(restartApp: () -> Unit) {
        onIsDoneBtnWorking(true)
        if (password.isBlank()) {
            onIsErrorPasswordChange(true)
            onIsDoneBtnWorking(false)
            //launchCatching { onShowSnackbar(empty_password_error, "") }
            return
        }

        launchCatching {
            accountService.authenticate(accountService.currentUserEmail, password)
            val uid = accountService.currentUserId
            val inputData = workDataOf(FirestoreServiceImpl.GENDER_FIELD to uid)
            val uploadRequest = OneTimeWorkRequestBuilder<LastSeenWorker>()
                .setInputData(inputData)
                .build()

            WorkManager.getInstance(context).enqueue(uploadRequest)
            firestoreService.deleteAccount(
                Delete(
                    id = accountService.currentUserId,
                    email = accountService.currentUserEmail,
                    text = text
                )
            )
            accountService.deleteAccount()
            restartApp()
        }
    }
    private fun onIsDoneBtnWorking(newValue: Boolean) { uiState.value = uiState.value.copy(isDoneBtnWorking = newValue) }
    private fun onIsErrorPasswordChange(newValue: Boolean) { uiState.value = uiState.value.copy(isErrorPassword = newValue) }
    fun onPasswordChange(newValue: String) { uiState.value = uiState.value.copy(password = newValue) }
    fun onTextChange(newValue: String) { uiState.value = uiState.value.copy(text = newValue) }
}
