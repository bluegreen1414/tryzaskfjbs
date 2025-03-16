/*
 * Copyright (C) 2024 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kapirti.social_chat_food_video.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.kapirti.social_chat_food_video.model.Theme
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.first

internal class LocalDataSourceImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : LocalDataSource {

    companion object {
        const val THEME_PREFERENCES_KEY = "theme_preferences_key"
    }

    private val themeUpdateFlow = MutableSharedFlow<Theme>()

    override suspend fun getThemeUpdate(): SharedFlow<Theme> = themeUpdateFlow

    override suspend fun publishThemeUpdate(theme: Theme) = themeUpdateFlow.emit(theme)

    override suspend fun getThemePreferences(): String {
        return runCatching {
            val preferencesKey = stringPreferencesKey(THEME_PREFERENCES_KEY)
            val preferences = dataStore.data.first()
            preferences[preferencesKey] ?: "Light"
        }.getOrElse { "Light" }
    }

    override suspend fun saveThemePreferences(theme: String) {
        val preferencesKey = stringPreferencesKey(THEME_PREFERENCES_KEY)
        dataStore.edit { preferences -> preferences[preferencesKey] = theme }
    }

}
