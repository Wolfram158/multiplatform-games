package ru.wolfram.client.domain.games.usecase

import ru.wolfram.client.domain.games.repository.GamesRepository

class GetGamesUseCase(
    private val repository: GamesRepository
) {
    suspend operator fun invoke(lang: String) =
        repository.getGames(lang)
}