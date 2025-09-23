package ru.wolfram.server.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class Message {
    @SerialName("Move")
    @Serializable
    data class Move(
        val x: Int,
        val y: Int,
        val key: String
    ) : Message()

    @SerialName("FirstMessage")
    @Serializable
    data class FirstMessage(
        val name: String,
        val desired: Who
    ) : Message()
}
