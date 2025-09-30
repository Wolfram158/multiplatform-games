package ru.wolfram.client.domain.tic_tac_toe.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import ru.wolfram.client.domain.tic_tac_toe.model.TicTacToeState
import ru.wolfram.client.domain.tic_tac_toe.model.Who

interface TicTacToeRepository {
    fun getTicTacToe(
        desired: Who
    ): StateFlow<TicTacToeState>

    suspend fun move(x: Int, y: Int)

}