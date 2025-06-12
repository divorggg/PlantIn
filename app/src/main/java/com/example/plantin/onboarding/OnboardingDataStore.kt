// OnboardingDataStore.kt
package com.example.plantin.onboarding

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

// Extension untuk membuat DataStore
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "onboarding_prefs")

object OnboardingDataStore {
    private val ONBOARDING_COMPLETED_KEY = booleanPreferencesKey("onboarding_completed")

    // Fungsi untuk menyimpan status onboarding selesai
    suspend fun setOnboardingCompleted(context: Context, completed: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[ONBOARDING_COMPLETED_KEY] = completed
        }
    }

    // Fungsi untuk mengecek apakah onboarding sudah selesai
    suspend fun isOnboardingCompleted(context: Context): Boolean {
        val preferences = context.dataStore.data.first()
        return preferences[ONBOARDING_COMPLETED_KEY] ?: false
    }

    // Flow untuk observe status onboarding (opsional)
    fun getOnboardingStatus(context: Context): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[ONBOARDING_COMPLETED_KEY] ?: false
        }
    }
}