package ru.wolfram.client.di

import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import ru.wolfram.client.data.network.common.ApiService
import ru.wolfram.client.data.tic_tac_toe.TicTacToeRepositoryImpl
import ru.wolfram.client.domain.tic_tac_toe.repository.TicTacToeRepository
import ru.wolfram.client.domain.tic_tac_toe.usecase.GetTicTacToeUseCase
import ru.wolfram.client.domain.tic_tac_toe.usecase.GetWhoResponseUseCase
import ru.wolfram.client.domain.tic_tac_toe.usecase.MoveUseCase
import ru.wolfram.client.domain.tic_tac_toe.usecase.RandomTicTacToeUseCase
import ru.wolfram.client.presentation.tic_tac_toe.TicTacToeViewModel

@Module
class TicTacToeModule {
    @Factory(binds = [TicTacToeRepository::class])
    fun getTicTacToeRepository(apiService: ApiService) = TicTacToeRepositoryImpl(apiService)

    @Factory
    fun getRandomTicTacToeUseCase(repository: TicTacToeRepository) =
        RandomTicTacToeUseCase(repository)

    @Factory
    fun getTicTacToeUseCase(repository: TicTacToeRepository) =
        GetTicTacToeUseCase(repository)

    @Factory
    fun getMoveUseCase(repository: TicTacToeRepository) =
        MoveUseCase(repository)

    @Factory
    fun getWhoResponseUseCase(repository: TicTacToeRepository) =
        GetWhoResponseUseCase(repository)

    @Factory
    fun getTicTacToeViewModel(
        randomTicTacToeUseCase: RandomTicTacToeUseCase,
        moveUseCase: MoveUseCase,
        getTicTacToeUseCase: GetTicTacToeUseCase,
        getWhoResponseUseCase: GetWhoResponseUseCase,
        dispatcher: CoroutineDispatcher
    ) =
        TicTacToeViewModel(
            randomTicTacToeUseCase,
            moveUseCase,
            getTicTacToeUseCase,
            getWhoResponseUseCase,
            dispatcher
        )
}