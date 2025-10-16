package ru.wolfram.client.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import org.koin.core.context.GlobalContext.get
import ru.wolfram.client.data.common.ClipboardManager
import ru.wolfram.client.data.network.common.ApiService
import ru.wolfram.client.data.tic_tac_toe.TicTacToeRepositoryImpl
import ru.wolfram.client.domain.tic_tac_toe.repository.TicTacToeRepository
import ru.wolfram.client.domain.tic_tac_toe.usecase.CopyPathToClipboardUseCase
import ru.wolfram.client.domain.tic_tac_toe.usecase.EnterTicTacToeUseCase
import ru.wolfram.client.domain.tic_tac_toe.usecase.GetTicTacToeUseCase
import ru.wolfram.client.domain.tic_tac_toe.usecase.LeaveUseCase
import ru.wolfram.client.domain.tic_tac_toe.usecase.NewTicTacToeUseCase
import ru.wolfram.client.presentation.tic_tac_toe.EnterTicTacToeViewModel
import ru.wolfram.client.presentation.tic_tac_toe.NewTicTacToeViewModel
import ru.wolfram.client.presentation.tic_tac_toe.TicTacToeViewModel

@Module
class TicTacToeModule {
    @Factory(binds = [TicTacToeRepository::class])
    fun getTicTacToeRepository(apiService: ApiService) =
        TicTacToeRepositoryImpl(
            apiService,
            get().get<DataStore<Preferences>>(),
            get().get<ClipboardManager>()
        )

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

    @Factory
    fun getNewTicTacToeUseCase(repository: TicTacToeRepository) = NewTicTacToeUseCase(repository)

    @Factory
    fun getCopyToClipboardUseCase(repository: TicTacToeRepository) =
        CopyPathToClipboardUseCase(repository)

    @Factory
    fun getNewTicTacToeViewModel(
        newTicTacToeUseCase: NewTicTacToeUseCase,
        leaveUseCase: LeaveUseCase,
        copyPathToClipboardUseCase: CopyPathToClipboardUseCase,
        ioDispatcher: CoroutineDispatcher
    ) = NewTicTacToeViewModel(
        newTicTacToeUseCase,
        leaveUseCase,
        copyPathToClipboardUseCase,
        ioDispatcher
    )

    @Factory
    fun getEnterTicTacToeUseCase(repository: TicTacToeRepository) =
        EnterTicTacToeUseCase(repository)

    @Factory
    fun getEnterTicTacToeViewModel(
        enterTicTacToeUseCase: EnterTicTacToeUseCase,
        leaveUseCase: LeaveUseCase,
        ioDispatcher: CoroutineDispatcher
    ) = EnterTicTacToeViewModel(
        enterTicTacToeUseCase,
        leaveUseCase,
        ioDispatcher
    )
}