package ru.wolfram.client.domain.tic_tac_toe.usecase

import ru.wolfram.client.domain.tic_tac_toe.repository.TicTacToeRepository

class LeaveUseCase(
    private val ticTacToeRepository: TicTacToeRepository
) {
    suspend operator fun invoke() = ticTacToeRepository.leave()
}