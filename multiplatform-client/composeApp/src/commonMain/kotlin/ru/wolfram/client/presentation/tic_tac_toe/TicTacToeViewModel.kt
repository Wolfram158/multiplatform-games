package ru.wolfram.client.presentation.tic_tac_toe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.plugins.websocket.receiveDeserialized
import io.ktor.client.plugins.websocket.sendSerialized
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.wolfram.client.data.mapper.toTicTacToeState
import ru.wolfram.client.data.network.tic_tac_toe.MessageDto
import ru.wolfram.client.data.network.tic_tac_toe.TicTacToeStateDto
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
    private val move = MutableStateFlow<Pair<Int, Int>?>(null)

    init {
        viewModelScope.launch(ioDispatcher) {
            getTicTacToeUseCase(Who.entries.random()) { who, webSocket ->
                viewModelScope.launch(ioDispatcher) {
                    while (true) {
                        _ticTacToe.update {
                            webSocket.receiveDeserialized<TicTacToeStateDto>().toTicTacToeState()
                        }
                    }
                }
                viewModelScope.launch(ioDispatcher) {
                    move.collect {
                        it?.let {
                            webSocket.sendSerialized(
                                MessageDto.MoveDto(
                                    it.first,
                                    it.second,
                                    who.key,
                                    "Move"
                                )
                            )
                        }
                    }
                }
            }
        }

    }

    private val _isMove = MutableStateFlow(false)
    val isMove = _isMove.asStateFlow()

    override fun handleAction(action: TicTacToeAction) {
        when (action) {
            is TicTacToeAction.MakeMove -> reduceMove(action)
        }
    }

    private fun reduceMove(action: TicTacToeAction.MakeMove) {
        viewModelScope.launch {
            move.update {
                Pair(action.x, action.y)
            }
        }
    }

}