package ru.wolfram.client.presentation.games

import ru.wolfram.client.presentation.common.Action

sealed interface GamesAction : Action {
    data class GetGames(val lang: String) : GamesAction
}