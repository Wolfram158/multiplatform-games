package ru.wolfram.client.domain.tic_tac_toe.usecase

import ru.wolfram.client.domain.tic_tac_toe.repository.TicTacToeRepository

class MoveUseCase(
    private val repository: TicTacToeRepository
) {
    suspend operator fun invoke(x: Int, y: Int, key: String) = repository.move(x, y, key)
}