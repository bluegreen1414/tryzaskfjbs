package com.kapirti.social_chat_food_video.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.kapirti.social_chat_food_video.core.constants.Cons.DEFAULT_COUNTRY_CODE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class CountryRepository (private val context: Context) {
    companion object PreferencesKey {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "country_pref")
        val COUNTRY_KEY = stringPreferencesKey(name = "country_code")
    }

    suspend fun saveCountry(country: String) {
        context.dataStore.edit { preferences ->
            preferences[COUNTRY_KEY] = country
        }
    }

    fun readCountry(): Flow<String> {
        return context.dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                val languageState = preferences[COUNTRY_KEY] ?: DEFAULT_COUNTRY_CODE
                languageState
            }
    }
}
