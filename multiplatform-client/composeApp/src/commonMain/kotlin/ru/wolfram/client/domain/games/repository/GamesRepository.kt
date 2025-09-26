package ru.wolfram.client.domain.games.repository

import ru.wolfram.client.domain.games.model.GamesResult

interface GamesRepository {
    suspend fun getGames(name: String, key: String, lang: String): GamesResult
}