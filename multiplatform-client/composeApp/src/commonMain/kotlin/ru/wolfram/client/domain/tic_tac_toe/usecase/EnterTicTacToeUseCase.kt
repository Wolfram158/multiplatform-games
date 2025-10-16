package ru.wolfram.client.domain.tic_tac_toe.usecase

import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import ru.wolfram.client.domain.tic_tac_toe.model.Who
import ru.wolfram.client.domain.tic_tac_toe.model.WhoResponseState
import ru.wolfram.client.domain.tic_tac_toe.repository.TicTacToeRepository

class EnterTicTacToeUseCase(
    private val repository: TicTacToeRepository
) {
    suspend operator fun invoke(
        path: String,
        desired: Who,
        callback: suspend (WhoResponseState.WhoResponse, DefaultClientWebSocketSession) -> Unit
    ) = repository.enterTicTacToe(path, desired, callback)
}