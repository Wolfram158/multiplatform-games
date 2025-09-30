package ru.wolfram.client.presentation.common

import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable
    object Auth : Route

    @Serializable
    data object Games : Route

    @Serializable
    data object TicTacToe : Route
}