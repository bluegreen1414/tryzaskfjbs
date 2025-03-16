package com.kapirti.social_chat_food_video.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class IsReviewDataStore(private val context: Context){
    companion object PreferencesKey {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "is_review_pref")
        val IS_REVIEW_KEY = booleanPreferencesKey(name = "review_completed")
    }

    suspend fun saveIsReviewState(type: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_REVIEW_KEY] = type
        }
    }

    fun readIsReviewState(): Flow<Boolean> {
        return context.dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                val editTypeState = preferences[IS_REVIEW_KEY] ?: false
                editTypeState
            }
    }
}
