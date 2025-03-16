package com.kapirti.social_chat_food_video.ui.presentation.settings.content.country

import com.kapirti.social_chat_food_video.core.datastore.CountryRepository
import com.kapirti.social_chat_food_video.ui.presentation.settings.content.feedback.FeedbackUiState
import androidx.compose.runtime.mutableStateOf
import com.kapirti.social_chat_food_video.model.service.LogService
import com.kapirti.social_chat_food_video.ui.presentation.KapirtiViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CountryViewModel @Inject constructor(
    private val countryRepository: CountryRepository,
    logService: LogService
): KapirtiViewModel(logService){
    var uiState = mutableStateOf(FeedbackUiState())
        private set

    private val _country = mutableStateOf<String?>(null)
    val country: String?
        get() = _country.value

    init {
        launchCatching {
            countryRepository.readCountry().collect { itc ->
                _country.value = itc
            }
        }
    }

    fun onCountryResponse(newValue: String) {
        launchCatching {
            onIsDoneBtnWorking(true)
            countryRepository.saveCountry(newValue)
            onIsDoneBtnWorking(false)
        }
    }

    private fun onIsDoneBtnWorking(newValue: Boolean) { uiState.value = uiState.value.copy(isDoneBtnWorking = newValue) }
}
