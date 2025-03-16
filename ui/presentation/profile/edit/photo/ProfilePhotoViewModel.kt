package com.kapirti.social_chat_food_video.ui.presentation.profile.edit.photo

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.kapirti.social_chat_food_video.model.service.AccountService
import com.kapirti.social_chat_food_video.model.service.LogService
import com.kapirti.social_chat_food_video.model.service.impl.FirestoreServiceImpl
import com.kapirti.social_chat_food_video.ui.presentation.KapirtiViewModel
import com.kapirti.social_chat_food_video.ui.presentation.camera.common.VideoUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ProfilePhotoViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val accountService: AccountService,
    logService: LogService
): KapirtiViewModel(logService){
    var uiState = mutableStateOf(VideoUiState())
        private set

    private val _selfieUri = mutableStateOf<Uri?>(null)
    val selfieUri
        get() = _selfieUri.value

    fun onSelfieResponse(uri: Uri) {
        _selfieUri.value = uri
    }



    fun savePhoto(popUp: () -> Unit) {
        launchCatching {
            onIsDoneBtnWorking(true)
            _selfieUri.value?.let {
                uploadReel(it.toString(), popUp)
            }
        }
    }
    private fun uploadReel(photo: String, popUp: () -> Unit) {
        val randomUid = UUID.randomUUID().toString()
        val uid = accountService.currentUserId
        val inputData = workDataOf(
            FirestoreServiceImpl.DESCRIPTION_FIELD to randomUid,
            FirestoreServiceImpl.GENDER_FIELD to uid,
            FirestoreServiceImpl.EXPLORE_COLLECTION to photo
        )

        val uploadRequest = OneTimeWorkRequestBuilder<ProfilePhotoWorker>()
            .setInputData(inputData)
            .build()

        WorkManager.getInstance(context).enqueue(uploadRequest)

        popUp()
    }

    private fun onIsDoneBtnWorking(newValue: Boolean) { uiState.value = uiState.value.copy(isDoneBtnWorking = newValue) }
}
