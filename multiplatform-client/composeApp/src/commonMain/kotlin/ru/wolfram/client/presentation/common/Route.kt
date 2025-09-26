package ru.wolfram.client.presentation.common

import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable
    object Auth : Route

    @Serializable
    data class Games(val name: String, val key: String) : Route

    @Serializable
    data class TicTacToe(val name: String, val key: String) : Route
}