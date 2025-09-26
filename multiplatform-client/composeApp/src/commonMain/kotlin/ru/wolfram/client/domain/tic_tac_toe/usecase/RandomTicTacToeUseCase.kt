package ru.wolfram.client.domain.tic_tac_toe.usecase

import ru.wolfram.client.domain.tic_tac_toe.repository.TicTacToeRepository

class RandomTicTacToeUseCase(
    private val repository: TicTacToeRepository
) {
    suspend operator fun invoke(name: String, key: String) = repository.randomTicTacToe(name, key)
}