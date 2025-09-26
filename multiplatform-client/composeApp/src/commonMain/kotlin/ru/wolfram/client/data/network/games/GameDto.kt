package ru.wolfram.client.data.network.games

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GameDto(
    @SerialName("name") val name: String,
    @SerialName("label") val labelDto: LabelDto,
    @SerialName("randomUrl") val randomUrl: String,
    @SerialName("createUrl") val createUrl: String
)