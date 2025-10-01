package ru.wolfram.client.presentation.tic_tac_toe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.websocket.receiveDeserialized
import io.ktor.client.plugins.websocket.sendSerialized
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.wolfram.client.data.mapper.toTicTacToeState
import ru.wolfram.client.data.network.tic_tac_toe.MessageDto
import ru.wolfram.client.data.network.tic_tac_toe.TicTacToeStateDto
import ru.wolfram.client.domain.common.isEnd
import ru.wolfram.client.domain.tic_tac_toe.model.Cell
import ru.wolfram.client.domain.tic_tac_toe.model.Error
import ru.wolfram.client.domain.tic_tac_toe.model.State
import ru.wolfram.client.domain.tic_tac_toe.model.TicTacToeState
import ru.wolfram.client.domain.tic_tac_toe.model.Who
import ru.wolfram.client.domain.tic_tac_toe.usecase.GetTicTacToeUseCase
import ru.wolfram.client.presentation.common.ActionHandler

class TicTacToeViewModel(
    private val getTicTacToeUseCase: GetTicTacToeUseCase,
    private val ioDispatcher: CoroutineDispatcher
) : ActionHandler<TicTacToeAction>, ViewModel() {
    private val _ticTacToe = MutableStateFlow(TicTacToeState())
    val ticTacToe = _ticTacToe.asStateFlow()
    private val move = MutableSharedFlow<Pair<Int, Int>>(replay = 1)
    var side = Who.entries.random()
        private set
    private val _isMove = MutableStateFlow(false)
    val isMove = _isMove.asStateFlow()
    private val _error = MutableStateFlow<Error>(Error.Empty)
    val error = _error.asStateFlow()

    init {
        launchGame()
    }

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

    private fun launchGame() {
        viewModelScope.launch(ioDispatcher) {
            try {
                getTicTacToeUseCase(side) { who, webSocket ->
                    viewModelScope.launch(ioDispatcher) {
                        side = who.who
                        _isMove.update {
                            side == ticTacToe.value.whoseMove
                        }
                        _ticTacToe.update { it ->
                            it.copy(
                                state = State.PROGRESS,
                                cells = List(3) { List(3) { Cell.EMPTY } })
                        }
                        try {
                            while (true) {
                                val state = webSocket.receiveDeserialized<TicTacToeStateDto>()
                                    .toTicTacToeState()
                                _isMove.update {
                                    side == state.whoseMove
                                }
                                _ticTacToe.update {
                                    state
                                }
                            }
                        } catch (e: CancellationException) {
                            throw e
                        } catch (_: Exception) {
                            if (!ticTacToe.value.isEnd()) {
                                _error.update {
                                    Error.UnexpectedEnd
                                }
                            }
                        }
                    }
                    viewModelScope.launch(ioDispatcher) {
                        move.collect {
                            try {
                                it.let {
                                    webSocket.sendSerialized(
                                        MessageDto.MoveDto(
                                            it.first,
                                            it.second,
                                            who.key,
                                            "Move"
                                        )
                                    )
                                }
                            } catch (e: CancellationException) {
                                throw e
                            } catch (_: Exception) {
                            }
                        }
                    }
                }
            } catch (_: HttpRequestTimeoutException) {
                _error.update {
                    Error.OpponentNotFound
                }
            }
        }
    }

}