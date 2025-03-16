package com.kapirti.social_chat_food_video.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.kapirti.social_chat_food_video.core.constants.Cons.DEFAULT_LANGUAGE_CODE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class LearnLanguageRepository(private val context: Context){
    companion object PreferencesKey {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "lp_pref")
        val LEARN_LANGUAGE_KEY = stringPreferencesKey(name = "lp")
    }

    suspend fun saveLearnLanguageState(type: String) {
        context.dataStore.edit { preferences ->
            preferences[LEARN_LANGUAGE_KEY] = type
        }
    }

    fun readLearnLanguageState(): Flow<String> {
        return context.dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                val editTypeState = preferences[LEARN_LANGUAGE_KEY] ?: DEFAULT_LANGUAGE_CODE
                editTypeState
            }
    }
}

