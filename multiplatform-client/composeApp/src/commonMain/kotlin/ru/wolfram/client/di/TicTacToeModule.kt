package ru.wolfram.client.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import org.koin.core.context.GlobalContext.get
import ru.wolfram.client.data.network.common.ApiService
import ru.wolfram.client.data.tic_tac_toe.TicTacToeRepositoryImpl
import ru.wolfram.client.domain.tic_tac_toe.repository.TicTacToeRepository
import ru.wolfram.client.domain.tic_tac_toe.usecase.GetTicTacToeUseCase
import ru.wolfram.client.domain.tic_tac_toe.usecase.LeaveUseCase
import ru.wolfram.client.presentation.tic_tac_toe.TicTacToeViewModel

@Module
class TicTacToeModule {
    @Factory(binds = [TicTacToeRepository::class])
    fun getTicTacToeRepository(apiService: ApiService) =
        TicTacToeRepositoryImpl(apiService, get().get<DataStore<Preferences>>())

    @Factory
    fun getTicTacToeUseCase(repository: TicTacToeRepository) =
        GetTicTacToeUseCase(repository)

    @Factory
    fun getLeaveUseCase(repository: TicTacToeRepository) =
        LeaveUseCase(repository)

    @Factory
    fun getTicTacToeViewModel(
        getTicTacToeUseCase: GetTicTacToeUseCase,
        leaveUseCase: LeaveUseCase,
        dispatcher: CoroutineDispatcher
    ) =
        TicTacToeViewModel(
            getTicTacToeUseCase,
            leaveUseCase,
            dispatcher
        )
}