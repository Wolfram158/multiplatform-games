package ru.wolfram.client.domain.tic_tac_toe.usecase

import ru.wolfram.client.domain.tic_tac_toe.model.Who
import ru.wolfram.client.domain.tic_tac_toe.repository.TicTacToeRepository

class HandshakeUseCase(
    private val repository: TicTacToeRepository
) {
    suspend operator fun invoke(name: String, desired: Who) =
        repository.handshake(name, desired)
}