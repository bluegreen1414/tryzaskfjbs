package com.kapirti.social_chat_food_video.core.datastore

import com.kapirti.social_chat_food_video.model.Theme
import kotlinx.coroutines.flow.SharedFlow

internal interface LocalDataSource {
    suspend fun getThemeUpdate(): SharedFlow<Theme>
    suspend fun publishThemeUpdate(theme: Theme)
    suspend fun getThemePreferences(): String
    suspend fun saveThemePreferences(theme: String)
}
