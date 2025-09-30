package ru.wolfram.client.presentation.tic_tac_toe

import ru.wolfram.client.presentation.common.Action

sealed interface TicTacToeAction : Action {
    data class MakeMove(val x: Int, val y: Int) : TicTacToeAction
}