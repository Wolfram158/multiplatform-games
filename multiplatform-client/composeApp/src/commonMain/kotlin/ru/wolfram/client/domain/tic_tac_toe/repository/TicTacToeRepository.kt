package ru.wolfram.client.domain.tic_tac_toe.repository

import kotlinx.coroutines.flow.Flow
import ru.wolfram.client.domain.common.GameCreationResult
import ru.wolfram.client.domain.tic_tac_toe.model.TicTacToeState
import ru.wolfram.client.domain.tic_tac_toe.model.Who
import ru.wolfram.client.domain.tic_tac_toe.model.WhoResponseState

interface TicTacToeRepository {
    suspend fun randomTicTacToe(name: String, key: String): GameCreationResult

    suspend fun connect(path: String)

    suspend fun handshake(name: String, desired: Who): WhoResponseState

    suspend fun move(x: Int, y: Int, key: String)

    fun getTicTacToe(): Flow<TicTacToeState>

}