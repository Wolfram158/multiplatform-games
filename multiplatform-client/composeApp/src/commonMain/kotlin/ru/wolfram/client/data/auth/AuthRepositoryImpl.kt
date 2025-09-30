package ru.wolfram.client.data.auth

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import ru.wolfram.client.data.common.KEY_KEY
import ru.wolfram.client.data.common.NAME_KEY
import ru.wolfram.client.data.mapper.toUserCreationResult
import ru.wolfram.client.data.network.common.ApiService
import ru.wolfram.client.domain.auth.model.UserCreationResult
import ru.wolfram.client.domain.auth.repository.AuthRepository

class AuthRepositoryImpl(
    private val apiService: ApiService,
    private val dataStore: DataStore<Preferences>
) : AuthRepository {
    private val name = stringPreferencesKey(NAME_KEY)
    private val key = stringPreferencesKey(KEY_KEY)

    override suspend fun auth(name: String): UserCreationResult {
        val result = apiService.auth(name, "ru").toUserCreationResult()
        if (result is UserCreationResult.UserKey) {
            saveUserInfo(result.name, result.key)
        }
        return result
    }

    private suspend fun saveUserInfo(name: String, key: String) {
        dataStore.updateData {
            it.toMutablePreferences().apply {
                set(this@AuthRepositoryImpl.name, name)
                set(this@AuthRepositoryImpl.key, key)
            }
        }
    }
}