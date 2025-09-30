package ru.wolfram.client.data.games

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.firstOrNull
import ru.wolfram.client.data.common.KEY_KEY
import ru.wolfram.client.data.common.NAME_KEY
import ru.wolfram.client.data.mapper.toGames
import ru.wolfram.client.data.network.common.ApiService
import ru.wolfram.client.domain.games.model.GamesResult
import ru.wolfram.client.domain.games.repository.GamesRepository

class GamesRepositoryImpl(
    private val apiService: ApiService,
    private val dataStore: DataStore<Preferences>
) : GamesRepository {
    private val nameKey = stringPreferencesKey(NAME_KEY)
    private val userKeyKey = stringPreferencesKey(KEY_KEY)

    override suspend fun getGames(
        lang: String
    ): GamesResult {
        return try {
            var varName: String? = null
            var varKey: String? = null
            dataStore.data.firstOrNull()?.let { prefs ->
                varName = prefs[nameKey]
                varKey = prefs[userKeyKey]
            }
            val name = varName
            val key = varKey
            if (name != null && key != null) {
                GamesResult.Games(apiService.getGames(name, key, lang).toGames())
            } else {
                throw RuntimeException("Unexpected behaviour when getting username or user key")
            }
        } catch (_: Exception) {
            GamesResult.Failure("Error occurred!")
        }
    }
}