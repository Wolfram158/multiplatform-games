package ru.wolfram.client.domain.games.repository

import ru.wolfram.client.domain.games.model.GamesResult

interface GamesRepository {
    suspend fun getGames(lang: String): GamesResult

    suspend fun leave()
}