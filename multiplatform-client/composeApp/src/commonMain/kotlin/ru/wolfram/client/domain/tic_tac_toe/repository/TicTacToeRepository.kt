package ru.wolfram.client.domain.tic_tac_toe.repository

import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import kotlinx.coroutines.flow.Flow
import ru.wolfram.client.domain.common.GameCreationResult
import ru.wolfram.client.domain.tic_tac_toe.model.Who
import ru.wolfram.client.domain.tic_tac_toe.model.WhoResponseState

interface TicTacToeRepository {
    suspend fun randomTicTacToe(name: String, key: String): GameCreationResult

    suspend fun connect(path: String)

    suspend fun handshake(name: String, desired: Who): WhoResponseState

    suspend fun move(x: Int, y: Int)

    suspend fun getTicTacToe(
        name: String,
        path: String,
        desired: Who,
        callback: suspend (WhoResponseState.WhoResponse, DefaultClientWebSocketSession) -> Unit
    )

    fun getWhoResponse(): Flow<WhoResponseState.WhoResponse?>

}