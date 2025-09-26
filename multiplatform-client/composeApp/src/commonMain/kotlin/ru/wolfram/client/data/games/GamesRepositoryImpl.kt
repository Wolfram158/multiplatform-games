package ru.wolfram.client.data.games

import ru.wolfram.client.data.mapper.toGames
import ru.wolfram.client.data.network.common.ApiService
import ru.wolfram.client.domain.games.model.GamesResult
import ru.wolfram.client.domain.games.repository.GamesRepository

class GamesRepositoryImpl(
    private val apiService: ApiService
) : GamesRepository {
    override suspend fun getGames(
        name: String,
        key: String,
        lang: String
    ): GamesResult {
        return try {
            GamesResult.Games(apiService.getGames(name, key, lang).toGames())
        } catch (_: Exception) {
            GamesResult.Failure("Error occurred!")
        }
    }
}