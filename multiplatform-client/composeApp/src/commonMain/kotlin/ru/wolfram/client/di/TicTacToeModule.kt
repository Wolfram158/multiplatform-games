package ru.wolfram.client.di

import kotlinx.coroutines.CoroutineDispatcher
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import ru.wolfram.client.data.network.common.ApiService
import ru.wolfram.client.data.tic_tac_toe.TicTacToeRepositoryImpl
import ru.wolfram.client.domain.tic_tac_toe.repository.TicTacToeRepository
import ru.wolfram.client.domain.tic_tac_toe.usecase.ConnectUseCase
import ru.wolfram.client.domain.tic_tac_toe.usecase.GetTicTacToeUseCase
import ru.wolfram.client.domain.tic_tac_toe.usecase.HandshakeUseCase
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
    fun getHandshakeUseCase(repository: TicTacToeRepository) =
        HandshakeUseCase(repository)

    @Factory
    fun getMoveUseCase(repository: TicTacToeRepository) =
        MoveUseCase(repository)

    @Factory
    fun getConnectUseCase(repository: TicTacToeRepository) =
        ConnectUseCase(repository)

    @KoinViewModel
    fun getTicTacToeViewModel(
        randomTicTacToeUseCase: RandomTicTacToeUseCase,
        connectUseCase: ConnectUseCase,
        handshakeUseCase: HandshakeUseCase,
        moveUseCase: MoveUseCase,
        getTicTacToeUseCase: GetTicTacToeUseCase,
        dispatcher: CoroutineDispatcher
    ) =
        TicTacToeViewModel(
            randomTicTacToeUseCase,
            connectUseCase,
            handshakeUseCase,
            moveUseCase,
            getTicTacToeUseCase,
            dispatcher
        )
}