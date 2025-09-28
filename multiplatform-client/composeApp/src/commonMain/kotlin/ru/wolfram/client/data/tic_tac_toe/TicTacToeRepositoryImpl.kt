package ru.wolfram.client.data.tic_tac_toe

import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.wolfram.client.data.mapper.toGameCreationResult
import ru.wolfram.client.data.mapper.toWhoDto
import ru.wolfram.client.data.mapper.toWhoResponse
import ru.wolfram.client.data.network.common.ApiService
import ru.wolfram.client.domain.common.GameCreationResult
import ru.wolfram.client.domain.tic_tac_toe.model.Who
import ru.wolfram.client.domain.tic_tac_toe.model.WhoResponseState
import ru.wolfram.client.domain.tic_tac_toe.repository.TicTacToeRepository

class TicTacToeRepositoryImpl(
    private val apiService: ApiService
) : TicTacToeRepository {
    override suspend fun randomTicTacToe(
        name: String,
        key: String
    ): GameCreationResult {
        return apiService.randomTicTacToe(name, key).toGameCreationResult()
    }

    override suspend fun connect(path: String) {
        apiService.connect(path)
    }

    override suspend fun handshake(name: String, desired: Who): WhoResponseState {
        return apiService.handshake(name, desired.toWhoDto()).toWhoResponse()
    }

    override suspend fun move(x: Int, y: Int) {
        apiService.move(x, y)
    }

    override suspend fun getTicTacToe(
        name: String,
        path: String,
        desired: Who,
        callback: suspend (WhoResponseState.WhoResponse, DefaultClientWebSocketSession) -> Unit
    ) {
        return apiService.getTicTacToe(name, path, desired.toWhoDto(), callback)
    }

    override fun getWhoResponse(): Flow<WhoResponseState.WhoResponse?> {
        return apiService.getWhoResponse().map { it?.toWhoResponse() }
    }
}