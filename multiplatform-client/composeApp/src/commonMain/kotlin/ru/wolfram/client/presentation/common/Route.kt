package ru.wolfram.client.presentation.common

import kotlinx.serialization.Serializable
import ru.wolfram.client.domain.games.model.Reason

sealed interface Route {
    @Serializable
    object Auth : Route

    @Serializable
    data object Games : Route

    @Serializable
    data class TicTacToe(val reason: Reason, val gameId: String? = null) : Route
}