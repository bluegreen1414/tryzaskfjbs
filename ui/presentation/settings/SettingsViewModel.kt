package com.kapirti.social_chat_food_video.ui.presentation.settings

import android.content.Context
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.State
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.compose.runtime.mutableStateOf
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.kapirti.social_chat_food_video.common.stateInUi
import com.kapirti.social_chat_food_video.core.constants.Cons.DEFAULT_COUNTRY_CODE
import com.kapirti.social_chat_food_video.core.constants.Cons.DEFAULT_LANGUAGE_CODE
import com.kapirti.social_chat_food_video.core.datastore.CountryRepository
import com.kapirti.social_chat_food_video.core.datastore.LearnLanguageRepository
import com.kapirti.social_chat_food_video.core.repository.SettingsRepository
import com.kapirti.social_chat_food_video.core.usecase.GetThemePreferencesUseCase
import com.kapirti.social_chat_food_video.core.usecase.GetThemeUpdateUseCase
import com.kapirti.social_chat_food_video.core.usecase.PublishThemeUpdateUseCase
import com.kapirti.social_chat_food_video.core.usecase.SaveThemePreferencesUseCase
import com.kapirti.social_chat_food_video.model.Theme
import com.kapirti.social_chat_food_video.model.service.AccountService
import com.kapirti.social_chat_food_video.model.service.LogService
import com.kapirti.social_chat_food_video.model.service.impl.FirestoreServiceImpl
import com.kapirti.social_chat_food_video.ui.presentation.KapirtiViewModel
import com.kapirti.social_chat_food_video.ui.presentation.main.LastSeenWorker
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map

@HiltViewModel
class SettingsViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val accountService: AccountService,
    private val countryRepository: CountryRepository,
    private val languageRepository: LearnLanguageRepository,
    private val settingsRepository: SettingsRepository,
    private val saveThemePreferencesUseCase: SaveThemePreferencesUseCase,
    private val publishThemeUpdateUseCase: PublishThemeUpdateUseCase,
    private val getThemePreferencesUseCase: GetThemePreferencesUseCase,
    private val getThemeUpdateUseCase: GetThemeUpdateUseCase,
    logService: LogService,
): KapirtiViewModel(logService) {
    val uiState = accountService.currentUser.map { SettingsUiState(it.isAnonymous) }
    val country = countryRepository.readCountry().stateInUi(DEFAULT_COUNTRY_CODE)
    val language = languageRepository.readLearnLanguageState().stateInUi(DEFAULT_LANGUAGE_CODE)

    private val _theme by lazy { mutableStateOf(Theme.Light) }
    val theme: State<Theme> by lazy { _theme.apply { updateTheme() } }

    init {
        launchCatching {
            getThemeUpdateUseCase().collect { _theme.value = it }
        }
    }

    private fun updateTheme() {
        launchCatching {
            _theme.value = Theme.valueOf(getThemePreferencesUseCase())
        }
    }


    fun onThemeChanged(theme: Theme) {
        launchCatching {
            saveThemePreferencesUseCase(theme.name)
            publishThemeUpdateUseCase(theme)
        }
    }


    fun share() { launchCatching { settingsRepository.share() } }
    fun rate() { launchCatching { settingsRepository.rate() } }

    fun onSignOutClick(restartApp: () -> Unit) {
        launchCatching {
            val uid = accountService.currentUserId
            val inputData = workDataOf(FirestoreServiceImpl.GENDER_FIELD to uid)
            val uploadRequest = OneTimeWorkRequestBuilder<LastSeenWorker>()
                .setInputData(inputData)
                .build()

            WorkManager.getInstance(context).enqueue(uploadRequest)

            accountService.signOut()
            restartApp()
        }
    }

    fun privacyPolicy(snackbarHostState: SnackbarHostState) {
        launchCatching {
            try {
                settingsRepository.privacyPolicy()
            } catch (e: Exception){
                snackbarHostState.showSnackbar(e.message ?: "")
            }
        }
    }
}
