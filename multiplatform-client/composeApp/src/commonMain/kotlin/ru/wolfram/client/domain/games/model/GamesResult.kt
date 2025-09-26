package ru.wolfram.client.domain.games.model

sealed interface GamesResult {
    object Initial : GamesResult
    object Progress : GamesResult
    data class Failure(val msg: String) : GamesResult
    data class Games(val games: List<Game>) : GamesResult
}