package ru.wolfram.client.di

import kotlinx.coroutines.CoroutineDispatcher
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import ru.wolfram.client.data.games.GamesRepositoryImpl
import ru.wolfram.client.data.network.common.ApiService
import ru.wolfram.client.domain.games.repository.GamesRepository
import ru.wolfram.client.domain.games.usecase.GetGamesUseCase
import ru.wolfram.client.presentation.games.GamesViewModel

@Module
class GamesModule {
    @Factory(binds = [GamesRepository::class])
    fun getGamesRepository(apiService: ApiService) = GamesRepositoryImpl(apiService)

    @Factory
    fun getGamesUseCase(repository: GamesRepository) = GetGamesUseCase(repository)

    @KoinViewModel
    fun getGamesViewModel(getGamesUseCase: GetGamesUseCase, dispatcher: CoroutineDispatcher) =
        GamesViewModel(getGamesUseCase, dispatcher)
}