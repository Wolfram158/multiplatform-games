package ru.wolfram.client.presentation.tic_tac_toe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.plugins.websocket.receiveDeserialized
import io.ktor.client.plugins.websocket.sendSerialized
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.wolfram.client.data.mapper.toTicTacToeState
import ru.wolfram.client.data.network.tic_tac_toe.MessageDto
import ru.wolfram.client.data.network.tic_tac_toe.TicTacToeStateDto
import ru.wolfram.client.domain.common.GameCreationResult
import ru.wolfram.client.domain.tic_tac_toe.model.TicTacToeState
import ru.wolfram.client.domain.tic_tac_toe.model.Who
import ru.wolfram.client.domain.tic_tac_toe.model.WhoResponseState
import ru.wolfram.client.domain.tic_tac_toe.usecase.ConnectUseCase
import ru.wolfram.client.domain.tic_tac_toe.usecase.GetTicTacToeUseCase
import ru.wolfram.client.domain.tic_tac_toe.usecase.GetWhoResponseUseCase
import ru.wolfram.client.domain.tic_tac_toe.usecase.HandshakeUseCase
import ru.wolfram.client.domain.tic_tac_toe.usecase.MoveUseCase
import ru.wolfram.client.domain.tic_tac_toe.usecase.RandomTicTacToeUseCase
import ru.wolfram.client.presentation.common.ActionHandler

class TicTacToeViewModel(
    private val randomTicTacToeUseCase: RandomTicTacToeUseCase,
    private val connectUseCase: ConnectUseCase,
    private val handshakeUseCase: HandshakeUseCase,
    private val moveUseCase: MoveUseCase,
    private val getTicTacToeUseCase: GetTicTacToeUseCase,
    getWhoResponseUseCase: GetWhoResponseUseCase,
    private val ioDispatcher: CoroutineDispatcher
) : ActionHandler<TicTacToeAction>, ViewModel() {
    private val _ticTacToe = MutableStateFlow(TicTacToeState())
    val ticTacToe = _ticTacToe.asStateFlow()
    private val move = MutableStateFlow<Pair<Int, Int>?>(null)

    suspend fun ticTacToe(name: String, path: String) {
        try {
            getTicTacToeUseCase(name, path, Who.NULL) { who, webSocket ->
                viewModelScope.launch {
                    while (true) {
                        _ticTacToe.update {
                            webSocket.receiveDeserialized<TicTacToeStateDto>().toTicTacToeState()
                        }
                    }
                }
                viewModelScope.launch {
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
        } catch (e: ClosedReceiveChannelException) {

        }
    }

    private val whoResponse =
        getWhoResponseUseCase().stateIn(viewModelScope, SharingStarted.Lazily, null)
    private val gamePath = MutableStateFlow<GameCreationResult>(GameCreationResult.Initial)
    private val gameKey = MutableStateFlow<WhoResponseState>(WhoResponseState.Initial)
    private val _isMove = MutableStateFlow(false)
    val isMove = _isMove.asStateFlow()

    override fun handleAction(action: TicTacToeAction) {
        when (action) {
            is TicTacToeAction.MakeMove -> reduceMove(action)
            is TicTacToeAction.RandomGameKey -> reduceRandomGameKey(action)
            is TicTacToeAction.Connect -> reduceConnect(action)
            is TicTacToeAction.Handshake -> reduceHandshake(action)
        }
    }

    private fun reduceRandomGameKey(action: TicTacToeAction.RandomGameKey) {
        viewModelScope.launch(ioDispatcher) {
            gamePath.update {
                GameCreationResult.Progress
            }
            gamePath.update {
                randomTicTacToeUseCase(action.name, action.key)
            }
            when (val res = gamePath.value) {
                is GameCreationResult.Failure -> {}
                is GameCreationResult.GameKey -> {
                    ticTacToe(action.name, res.key)
                }

                GameCreationResult.Initial -> {}
                GameCreationResult.Progress -> {}
            }
        }
    }

    private fun reduceConnect(action: TicTacToeAction.Connect) {
        viewModelScope.launch(ioDispatcher) {
            connectUseCase(action.path)
        }
    }

    private fun reduceHandshake(action: TicTacToeAction.Handshake) {
        viewModelScope.launch(ioDispatcher) {
            gameKey.update {
                handshakeUseCase(action.name, action.desired)
            }
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