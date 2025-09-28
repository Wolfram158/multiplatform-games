package ru.wolfram.client.data.network.tic_tac_toe

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed class MessageDto {
    @Serializable
    data class MoveDto(
        @SerialName("x") val x: Int,
        @SerialName("y") val y: Int,
        @SerialName("key") val key: String,
        @SerialName("type") val type: String
    ) : MessageDto()

    @Serializable
    data class FirstMessageDto(
        @SerialName("name") val name: String,
        @SerialName("desired") val desired: WhoDto,
        @SerialName("type") val type: String
    ) : MessageDto()
}