package ru.wolfram.client.presentation.tic_tac_toe

import androidx.lifecycle.viewModelScope
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.websocket.receiveDeserialized
import io.ktor.client.plugins.websocket.sendSerialized
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.wolfram.client.data.mapper.toTicTacToeState
import ru.wolfram.client.data.network.tic_tac_toe.MessageDto
import ru.wolfram.client.data.network.tic_tac_toe.TicTacToeStateDto
import ru.wolfram.client.domain.common.isEnd
import ru.wolfram.client.domain.tic_tac_toe.model.Cell
import ru.wolfram.client.domain.tic_tac_toe.model.Error
import ru.wolfram.client.domain.tic_tac_toe.model.State
import ru.wolfram.client.domain.tic_tac_toe.usecase.CopyPathToClipboardUseCase
import ru.wolfram.client.domain.tic_tac_toe.usecase.LeaveUseCase
import ru.wolfram.client.domain.tic_tac_toe.usecase.NewTicTacToeUseCase
import ru.wolfram.client.presentation.common.ActionHandler

class NewTicTacToeViewModel(
    private val newTicTacToeUseCase: NewTicTacToeUseCase,
    leaveUseCase: LeaveUseCase,
    private val copyPathToClipboardUseCase: CopyPathToClipboardUseCase,
    private val ioDispatcher: CoroutineDispatcher
) : ActionHandler<TicTacToeAction>, AbstractTicTacToeViewModel(leaveUseCase, ioDispatcher) {
    override fun launchGame(path: String?) {
        viewModelScope.launch(ioDispatcher) {
            try {
                newTicTacToeUseCase(side) { who, webSocket ->
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
            } catch (_: Exception) {
                _error.update {
                    Error.UnexpectedEnd
                }
            }
        }
    }

    init {
        launchGame()
    }

    suspend fun copyPathToClipboard(): Boolean {
        return withContext(ioDispatcher) {
            copyPathToClipboardUseCase()
        }
    }

}