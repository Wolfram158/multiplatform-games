package ru.wolfram.client.data.network.tic_tac_toe

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class CellDto {
    @SerialName("CROSS")
    CROSS,
    @SerialName("NULL")
    NULL,
    @SerialName("EMPTY")
    EMPTY
}