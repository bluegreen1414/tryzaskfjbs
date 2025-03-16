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

package com.kapirti.social_chat_food_video.core.repository

import com.kapirti.social_chat_food_video.core.datastore.LocalDataSource
import com.kapirti.social_chat_food_video.model.Theme
import javax.inject.Inject
import kotlinx.coroutines.flow.SharedFlow

internal class UserPreferencesRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource
) : UserPreferencesRepository {
    override suspend fun getThemeUpdate(): SharedFlow<Theme> = localDataSource.getThemeUpdate()

    override suspend fun publishThemeUpdate(theme: Theme) =
        localDataSource.publishThemeUpdate(theme)

    override suspend fun getThemePreferences(): String = localDataSource.getThemePreferences()

    override suspend fun saveThemePreferences(theme: String) =
        localDataSource.saveThemePreferences(theme)

}
