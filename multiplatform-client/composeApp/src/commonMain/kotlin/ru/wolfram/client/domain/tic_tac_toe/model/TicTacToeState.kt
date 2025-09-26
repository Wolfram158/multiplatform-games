package ru.wolfram.client.domain.tic_tac_toe.model

data class TicTacToeState(
    val state: State = State.PROGRESS,
    val winner: String? = null,
    val loser: String? = null,
    val whoseMove: Who = Who.NULL,
    val cells: List<List<Cell>> = List(3) { List(3) { Cell.EMPTY } }
)