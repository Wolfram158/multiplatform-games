package ru.wolfram.client.data.network.tic_tac_toe

import kotlinx.serialization.Serializable

@Serializable
enum class StateDto {
    PROGRESS,
    WIN_FAILURE,
    DRAW
}