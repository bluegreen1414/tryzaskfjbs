package com.kapirti.social_chat_food_video.ui.presentation.settings.content.language

import com.kapirti.social_chat_food_video.core.datastore.CountryRepository
import com.kapirti.social_chat_food_video.ui.presentation.settings.content.feedback.FeedbackUiState
import androidx.compose.runtime.mutableStateOf
import com.kapirti.social_chat_food_video.core.datastore.LearnLanguageRepository
import com.kapirti.social_chat_food_video.model.service.LogService
import com.kapirti.social_chat_food_video.ui.presentation.KapirtiViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LanguageViewModel @Inject constructor(
    private val languageRepository: LearnLanguageRepository,
    logService: LogService
): KapirtiViewModel(logService){
    var uiState = mutableStateOf(FeedbackUiState())
        private set

    private val _language = mutableStateOf<String?>(null)
    val language: String?
        get() = _language.value

    init {
        launchCatching {
            languageRepository.readLearnLanguageState().collect { itc ->
                _language.value = itc
            }
        }
    }

    fun onCountryResponse(newValue: String) {
        launchCatching {
            onIsDoneBtnWorking(true)
            languageRepository.saveLearnLanguageState(newValue)
            onIsDoneBtnWorking(false)
        }
    }

    private fun onIsDoneBtnWorking(newValue: Boolean) { uiState.value = uiState.value.copy(isDoneBtnWorking = newValue) }
}
