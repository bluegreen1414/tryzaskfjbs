package com.kapirti.social_chat_food_video.ui.presentation.main

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.kapirti.social_chat_food_video.core.usecase.GetThemePreferencesUseCase
import com.kapirti.social_chat_food_video.core.usecase.GetThemeUpdateUseCase
import com.kapirti.social_chat_food_video.model.Theme
import com.kapirti.social_chat_food_video.model.service.AccountService
import com.kapirti.social_chat_food_video.model.service.FirestoreService
import com.kapirti.social_chat_food_video.model.service.LogService
import com.kapirti.social_chat_food_video.model.service.impl.FirestoreServiceImpl
import com.kapirti.social_chat_food_video.ui.presentation.KapirtiViewModel
import com.kapirti.social_chat_food_video.ui.presentation.settings.SettingsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.flow.map

@HiltViewModel
class MainViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val firestoreService: FirestoreService,
    private val accountService: AccountService,
    private val getThemePreferencesUseCase: GetThemePreferencesUseCase,
    private val getThemeUpdateUseCase: GetThemeUpdateUseCase,
    logService: LogService
) : KapirtiViewModel(logService) {
    val uiState = accountService.currentUser.map { SettingsUiState(it.isAnonymous, it.email) }
    private val _theme by lazy { mutableStateOf(Theme.Light) }
    val theme: State<Theme> by lazy { _theme.apply { updateTheme() } }

    init {
        launchCatching {
            getThemeUpdateUseCase().collect {
                _theme.value = it
            }
        }
    }


    private fun updateTheme() {
        launchCatching {
            _theme.value = Theme.valueOf(getThemePreferencesUseCase())
        }
    }


    fun saveIsOnline() {
        launchCatching {
            uiState.collect {
                if (!it.isAnonymousAccount) {
                    val uid = accountService.currentUserId
                    val inputData = workDataOf(
                        FirestoreServiceImpl.GENDER_FIELD to uid,
                    )

                    val uploadRequest = OneTimeWorkRequestBuilder<OnlineWorker>()
                        .setInputData(inputData)
                        .build()

                    WorkManager.getInstance(context).enqueue(uploadRequest)
                }
            }
        }
    }

    fun saveLastSeen() {
        launchCatching {
            uiState.collect {
                if (!it.isAnonymousAccount) {
                    val uid = accountService.currentUserId
                    val inputData = workDataOf(
                        FirestoreServiceImpl.GENDER_FIELD to uid,
                    )

                    val uploadRequest = OneTimeWorkRequestBuilder<LastSeenWorker>()
                        .setInputData(inputData)
                        .build()

                    WorkManager.getInstance(context).enqueue(uploadRequest)
                }
            }
        }
    }
}
