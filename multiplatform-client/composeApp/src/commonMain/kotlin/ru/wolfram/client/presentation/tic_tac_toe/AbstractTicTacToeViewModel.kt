package ru.wolfram.client.presentation.tic_tac_toe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.wolfram.client.domain.tic_tac_toe.model.Error
import ru.wolfram.client.domain.tic_tac_toe.model.TicTacToeState
import ru.wolfram.client.domain.tic_tac_toe.model.Who
import ru.wolfram.client.domain.tic_tac_toe.usecase.LeaveUseCase
import ru.wolfram.client.presentation.common.ActionHandler

abstract class AbstractTicTacToeViewModel(
    private val leaveUseCase: LeaveUseCase,
    private val ioDispatcher: CoroutineDispatcher
) : ActionHandler<TicTacToeAction>, ViewModel() {
    protected val _ticTacToe = MutableStateFlow(TicTacToeState())
    val ticTacToe = _ticTacToe.asStateFlow()
    protected val move = MutableSharedFlow<Pair<Int, Int>>(replay = 1)
    var side = Who.entries.random()
        protected set
    protected val _isMove = MutableStateFlow(false)
    val isMove = _isMove.asStateFlow()
    protected val _error =
        MutableStateFlow<Error>(Error.Empty)
    val error = _error.asStateFlow()

    override fun handleAction(action: TicTacToeAction) {
        when (action) {
            is TicTacToeAction.MakeMove -> reduceMove(action)
            TicTacToeAction.Retry -> reduceRetry()
        }
    }

    private fun reduceMove(action: TicTacToeAction.MakeMove) {
        viewModelScope.launch(ioDispatcher) {
            _isMove.update {
                false
            }
            move.emit(Pair(action.x, action.y))
        }
    }

    private fun reduceRetry() {
        _error.update {
            Error.Empty
        }
        launchGame()
    }

    protected abstract fun launchGame(path: String? = null)

    override fun onCleared() {
        viewModelScope.launch(ioDispatcher) {
            try {
                leaveUseCase()
            } catch (_: Exception) {
            }
        }
        super.onCleared()
    }
}