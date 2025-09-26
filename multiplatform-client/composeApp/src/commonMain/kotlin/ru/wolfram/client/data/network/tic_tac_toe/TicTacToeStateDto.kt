package ru.wolfram.client.data.network.tic_tac_toe

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TicTacToeStateDto(
    @SerialName("state") val stateDto: StateDto = StateDto.PROGRESS,
    @SerialName("winner") val winner: String? = null,
    @SerialName("loser") val loser: String? = null,
    @SerialName("whoseMove") val whoseMove: WhoDto = WhoDto.NULL,
    @SerialName("cells") val cellDtos: List<List<CellDto>> = List(3) { List(3) { CellDto.EMPTY } }
)