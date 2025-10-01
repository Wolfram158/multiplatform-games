package ru.wolfram.client.domain.games.usecase

import ru.wolfram.client.domain.games.repository.GamesRepository

class LeaveUseCase(
    private val repository: GamesRepository
) {
    suspend operator fun invoke() = repository.leave()
}