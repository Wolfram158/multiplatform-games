package ru.wolfram.client.presentation.common

import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable
    object Auth : Route

    @Serializable
    object Games : Route
}