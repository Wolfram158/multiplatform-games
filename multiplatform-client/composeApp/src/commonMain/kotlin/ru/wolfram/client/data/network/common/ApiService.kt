package ru.wolfram.client.data.network.common

import kotlinx.coroutines.flow.Flow
import ru.wolfram.client.data.network.auth.UserCreationResultDto
import ru.wolfram.client.data.network.games.GameDto
import ru.wolfram.client.data.network.tic_tac_toe.TicTacToeStateDto
import ru.wolfram.client.data.network.tic_tac_toe.WhoDto
import ru.wolfram.client.data.network.tic_tac_toe.WhoResponseDto

interface ApiService {
    suspend fun auth(name: String, lang: String): UserCreationResultDto

    suspend fun getGames(name: String, key: String, lang: String): List<GameDto>

    suspend fun connect(path: String)

    suspend fun randomTicTacToe(name: String, key: String): GameCreationResultDto

    suspend fun handshake(name: String, desired: WhoDto): WhoResponseDto

    suspend fun move(x: Int, y: Int, key: String)

    val ticTacToe: Flow<TicTacToeStateDto>
}