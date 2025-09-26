package ru.wolfram.client.data.network.tic_tac_toe

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WhoResponseDto(
    @SerialName("whoDto") val whoDto: WhoDto,
    @SerialName("key") val key: String
)