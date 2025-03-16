package com.kapirti.social_chat_food_video.ui.presentation.welcome

import com.kapirti.social_chat_food_video.core.datastore.OnBoardingRepository
import com.kapirti.social_chat_food_video.model.service.LogService
import com.kapirti.social_chat_food_video.ui.presentation.KapirtiViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    logService: LogService,
    private val repository: OnBoardingRepository,
): KapirtiViewModel(logService) {

    fun saveOnBoardingState(navigateAndPopUpWelcomeToLogin: () -> Unit) {
        launchCatching {
            repository.saveOnBoardingState(completed = true)
            navigateAndPopUpWelcomeToLogin()
           // langRepository.saveLangState(Locale.getDefault().getDisplayLanguage())
           // openAndPopUp(HOME_SCREEN, WELCOME_SCREEN)
        }
    }
}
