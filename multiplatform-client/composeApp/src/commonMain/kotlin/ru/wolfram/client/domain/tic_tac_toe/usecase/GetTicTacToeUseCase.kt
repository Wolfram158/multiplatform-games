package ru.wolfram.client.domain.tic_tac_toe.usecase

import ru.wolfram.client.domain.tic_tac_toe.repository.TicTacToeRepository

class GetTicTacToeUseCase(
    private val repository: TicTacToeRepository
) {
    operator fun invoke() = repository.getTicTacToe()
}