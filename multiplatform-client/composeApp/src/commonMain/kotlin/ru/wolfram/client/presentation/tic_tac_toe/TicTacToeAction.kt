package ru.wolfram.client.presentation.tic_tac_toe

import ru.wolfram.client.domain.tic_tac_toe.model.Who
import ru.wolfram.client.presentation.common.Action

sealed interface TicTacToeAction : Action {
    data class MakeMove(val x: Int, val y: Int) : TicTacToeAction
    data class RandomGameKey(val name: String, val key: String) : TicTacToeAction
    data class Handshake(val name: String, val desired: Who) : TicTacToeAction
    data class Connect(val path: String) : TicTacToeAction
}