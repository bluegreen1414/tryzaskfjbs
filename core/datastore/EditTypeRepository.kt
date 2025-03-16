package com.kapirti.social_chat_food_video.core.datastore

import android.content.Context
import android.provider.ContactsContract.Directory.ACCOUNT_TYPE
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class EditTypeRepository(private val context: Context){
    companion object PreferencesKey {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "edit_type_pref")
        val EDIT_TYPE_KEY = stringPreferencesKey(name = "edit_type_completed")
    }

    suspend fun saveEditTypeState(type: String) {
        context.dataStore.edit { preferences ->
            preferences[EDIT_TYPE_KEY] = type
        }
    }

    fun readEditTypeState(): Flow<String> {
        return context.dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                val editTypeState = preferences[EDIT_TYPE_KEY] ?: ACCOUNT_TYPE
                editTypeState
            }
    }
}
