package ru.wolfram.client.domain.tic_tac_toe.usecase

import ru.wolfram.client.domain.tic_tac_toe.repository.TicTacToeRepository

class CopyPathToClipboardUseCase(
    private val repository: TicTacToeRepository
) {
    suspend operator fun invoke() = repository.copyPathToClipboard()
}