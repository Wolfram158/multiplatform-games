package ru.wolfram.client.data.network.tic_tac_toe

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class StateDto {
    @SerialName("PROGRESS")
    PROGRESS,
    @SerialName("WIN_FAILURE")
    WIN_FAILURE,
    @SerialName("DRAW")
    DRAW
}