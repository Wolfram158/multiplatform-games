package ru.wolfram.client.data.network.common

import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import ru.wolfram.client.data.network.auth.UserCreationResultDto
import ru.wolfram.client.data.network.games.GameDto
import ru.wolfram.client.data.network.tic_tac_toe.WhoDto
import ru.wolfram.client.domain.tic_tac_toe.model.WhoResponseState

interface ApiService {
    suspend fun auth(name: String, lang: String): UserCreationResultDto

    suspend fun getGames(name: String, key: String, lang: String): List<GameDto>

    suspend fun randomTicTacToe(name: String, key: String): GameCreationResultDto

    suspend fun getTicTacToe(
        name: String,
        path: String,
        desired: WhoDto,
        callback: suspend (WhoResponseState.WhoResponse, DefaultClientWebSocketSession) -> Unit
    )

    suspend fun newTicTacToe(name: String, key: String): GameCreationResultDto

    suspend fun leave(name: String, key: String)

    suspend fun login(name: String, key: String): UserCreationResultDto

    suspend fun leaveGameSession(name: String, key: String)

    suspend fun validateTicTacToe(name: String, key: String, path: String): Boolean

}