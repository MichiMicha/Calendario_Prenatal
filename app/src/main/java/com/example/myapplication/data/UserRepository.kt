package com.example.myapplication.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

val Context.dataStore by preferencesDataStore(name = "user_prefs")

class UserPreferences(val context: Context) {

    companion object {
        val USER_NAME_KEY = stringPreferencesKey("user_name")
        val USER_WEIGHT_KEY = stringPreferencesKey("user_weight")
        val PREGNANCY_DATE_KEY = stringPreferencesKey("pregnancy_date")
    }

    // Guardamos solo los tres datos necesarios
    suspend fun saveUserData(name: String, weight: String, pregnancyDate: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_NAME_KEY] = name
            preferences[USER_WEIGHT_KEY] = weight
            preferences[PREGNANCY_DATE_KEY] = pregnancyDate
        }
    }
}