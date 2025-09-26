package ru.wolfram.client.data.network.tic_tac_toe

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class WhoDto {
    @SerialName("CROSS")
    CROSS,

    @SerialName("NULL")
    NULL
}