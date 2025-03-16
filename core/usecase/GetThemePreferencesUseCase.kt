package com.kapirti.social_chat_food_video.core.usecase

import com.kapirti.social_chat_food_video.core.repository.UserPreferencesRepository
import javax.inject.Inject

class GetThemePreferencesUseCase @Inject constructor(
    private val repository: UserPreferencesRepository
) {
    suspend operator fun invoke(): String = repository.getThemePreferences()
}
