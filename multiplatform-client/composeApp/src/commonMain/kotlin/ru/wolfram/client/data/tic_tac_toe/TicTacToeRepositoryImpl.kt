package ru.wolfram.client.data.tic_tac_toe

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import kotlinx.coroutines.flow.firstOrNull
import ru.wolfram.client.data.common.KEY_KEY
import ru.wolfram.client.data.common.NAME_KEY
import ru.wolfram.client.data.common.PATH_KEY
import ru.wolfram.client.data.mapper.toGameCreationResult
import ru.wolfram.client.data.mapper.toWhoDto
import ru.wolfram.client.data.network.common.ApiService
import ru.wolfram.client.domain.common.GameCreationResult
import ru.wolfram.client.domain.tic_tac_toe.model.Who
import ru.wolfram.client.domain.tic_tac_toe.model.WhoResponseState
import ru.wolfram.client.domain.tic_tac_toe.repository.TicTacToeRepository

class TicTacToeRepositoryImpl(
    private val apiService: ApiService,
    private val dataStore: DataStore<Preferences>
) : TicTacToeRepository {
    private val nameKey = stringPreferencesKey(NAME_KEY)
    private val userKeyKey = stringPreferencesKey(KEY_KEY)
    private val pathKey = stringPreferencesKey(PATH_KEY)

    override suspend fun randomTicTacToe(): GameCreationResult {
        var varName: String? = null
        var varKey: String? = null
        dataStore.data.firstOrNull()?.let { prefs ->
            varName = prefs[nameKey]
            varKey = prefs[userKeyKey]
        }
        val key = varKey
        val name = varName
        if (name != null && key != null) {
            val result = apiService.randomTicTacToe(name, key).toGameCreationResult()
            if (result is GameCreationResult.GameKey) {
                dataStore.updateData {
                    it.toMutablePreferences().apply {
                        set(pathKey, result.key)
                    }
                }
            }
            return result
        }
        throw RuntimeException("Unexpected behaviour when getting name or user key from DataStore")
    }

    override suspend fun getTicTacToe(
        desired: Who,
        callback: suspend (WhoResponseState.WhoResponse, DefaultClientWebSocketSession) -> Unit
    ) {
        randomTicTacToe()
        var varName: String? = null
        var varPath: String? = null
        dataStore.data.firstOrNull()?.let { prefs ->
            varName = prefs[nameKey]
            varPath = prefs[pathKey]
        }
        val path = varPath
        val name = varName
        if (name != null && path != null) {
            return apiService.getTicTacToe(name, path, desired.toWhoDto(), callback)
        }
        throw RuntimeException("Unexpected behaviour when getting name or game path from DataStore")
    }

}