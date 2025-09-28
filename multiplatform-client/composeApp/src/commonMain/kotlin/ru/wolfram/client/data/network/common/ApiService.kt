package ru.wolfram.client.data.network.common

import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import kotlinx.coroutines.flow.StateFlow
import ru.wolfram.client.data.network.auth.UserCreationResultDto
import ru.wolfram.client.data.network.games.GameDto
import ru.wolfram.client.data.network.tic_tac_toe.WhoDto
import ru.wolfram.client.data.network.tic_tac_toe.WhoResponseDto
import ru.wolfram.client.domain.tic_tac_toe.model.WhoResponseState

interface ApiService {
    suspend fun auth(name: String, lang: String): UserCreationResultDto

    suspend fun getGames(name: String, key: String, lang: String): List<GameDto>

    suspend fun connect(path: String)

    suspend fun randomTicTacToe(name: String, key: String): GameCreationResultDto

    suspend fun handshake(name: String, desired: WhoDto): WhoResponseDto

    suspend fun move(x: Int, y: Int)

    suspend fun getTicTacToe(
        name: String,
        path: String,
        desired: WhoDto,
        callback: suspend (WhoResponseState.WhoResponse, DefaultClientWebSocketSession) -> Unit
    )

    fun getWhoResponse(): StateFlow<WhoResponseDto?>
}