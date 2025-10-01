package ru.wolfram.client.domain.common

import ru.wolfram.client.domain.tic_tac_toe.model.State
import ru.wolfram.client.domain.tic_tac_toe.model.TicTacToeState

fun TicTacToeState.isEnd() = state.isEnd()

fun State.isEnd() = this != State.INITIAL && this != State.PROGRESS