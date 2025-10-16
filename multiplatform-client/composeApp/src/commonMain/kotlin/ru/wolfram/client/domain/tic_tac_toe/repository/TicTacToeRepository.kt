package ru.wolfram.client.domain.tic_tac_toe.repository

import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import ru.wolfram.client.domain.common.GameCreationResult
import ru.wolfram.client.domain.tic_tac_toe.model.Who
import ru.wolfram.client.domain.tic_tac_toe.model.WhoResponseState

interface TicTacToeRepository {
    suspend fun randomTicTacToe(): GameCreationResult

    suspend fun getTicTacToe(
        desired: Who,
        callback: suspend (WhoResponseState.WhoResponse, DefaultClientWebSocketSession) -> Unit
    )

    suspend fun leave()

    suspend fun newTicTacToe(
        desired: Who,
        callback: suspend (WhoResponseState.WhoResponse, DefaultClientWebSocketSession) -> Unit
    )

    suspend fun copyPathToClipboard(): Boolean

    suspend fun enterTicTacToe(
        path: String,
        desired: Who,
        callback: suspend (WhoResponseState.WhoResponse, DefaultClientWebSocketSession) -> Unit
    )

}