package ru.wolfram.client.domain.tic_tac_toe.usecase

import ru.wolfram.client.domain.tic_tac_toe.repository.TicTacToeRepository

class ConnectUseCase(
    private val repository: TicTacToeRepository
) {
    suspend operator fun invoke(path: String) = repository.connect(path)
}