package ru.wolfram.client.data.auth

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.firstOrNull
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
    private val nameKey = stringPreferencesKey(NAME_KEY)
    private val userKeyKey = stringPreferencesKey(KEY_KEY)

    override suspend fun auth(name: String): UserCreationResult {
        val result = apiService.auth(name, "ru").toUserCreationResult()
        if (result is UserCreationResult.UserKey) {
            saveUserInfo(result.name, result.key)
        }
        return result
    }

    override suspend fun login(): UserCreationResult {
        var varName: String? = null
        var varKey: String? = null
        dataStore.data.firstOrNull()?.let { prefs ->
            varName = prefs[nameKey]
            varKey = prefs[userKeyKey]
        }
        val name = varName
        val key = varKey
        if (name != null && key != null) {
            val result = apiService.login(name, key).toUserCreationResult()
            if (result is UserCreationResult.UserKey) {
                dataStore.edit {
                    it[userKeyKey] = result.key
                }
            }
            return result
        }
        return UserCreationResult.Failure("")
    }

    override suspend fun getAlreadyUsedName(): String? {
        var varName: String? = null
        dataStore.data.firstOrNull()?.let { prefs ->
            varName = prefs[nameKey]
        }
        return varName
    }

    private suspend fun saveUserInfo(name: String, key: String) {
        dataStore.updateData {
            it.toMutablePreferences().apply {
                set(this@AuthRepositoryImpl.nameKey, name)
                set(this@AuthRepositoryImpl.userKeyKey, key)
            }
        }
    }
}
