package com.example.personalproject.data

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import com.example.personalproject.PreferenceKeys
import com.example.personalproject.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Singleton

@Singleton
class DataStoreManager(private val context: Context) {

    suspend fun saveAuthData(token: String, userId: String, role: String) {
        context.dataStore.edit { prefs ->
            prefs[PreferenceKeys.TOKEN] = token
            prefs[PreferenceKeys.USER_ID] = userId
            prefs[PreferenceKeys.ROLE] = role
        }
    }

    val tokenFlow: Flow<String?> = context.dataStore.data.map { it[PreferenceKeys.TOKEN] }
    val roleFlow: Flow<String?> = context.dataStore.data.map { it[PreferenceKeys.ROLE] }
    val userIdFlow: Flow<String?> = context.dataStore.data.map { it[PreferenceKeys.USER_ID] }

    suspend fun clear() {
        println(tokenFlow.firstOrNull())
        println(roleFlow.firstOrNull())
        println(userIdFlow.firstOrNull())
        context.dataStore.edit { it.clear() }
        println(tokenFlow.firstOrNull())
        println(roleFlow.firstOrNull())
        println(userIdFlow.firstOrNull())
    }

}
