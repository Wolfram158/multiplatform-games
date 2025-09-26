package ru.wolfram.client.presentation.games

import ru.wolfram.client.presentation.common.Action

sealed interface GamesAction : Action {
    data class GetGames(val name: String, val key: String, val lang: String) : GamesAction
}