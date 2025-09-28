package ru.wolfram.server.model

import kotlinx.serialization.Serializable

@Serializable
data class TicTacToeState(
    val state: State = State.PROGRESS,
    val winner: String? = null,
    val loser: String? = null,
    val whoseMove: Who = Who.NULL,
    val cells: List<List<Cell>> = List(3) { List(3) { Cell.EMPTY } }
)
