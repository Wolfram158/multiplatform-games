package ru.wolfram.client.presentation.tic_tac_toe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.wolfram.client.domain.common.GameCreationResult
import ru.wolfram.client.domain.tic_tac_toe.model.TicTacToeState
import ru.wolfram.client.domain.tic_tac_toe.model.Who
import ru.wolfram.client.domain.tic_tac_toe.model.WhoResponseState
import ru.wolfram.client.domain.tic_tac_toe.usecase.ConnectUseCase
import ru.wolfram.client.domain.tic_tac_toe.usecase.GetTicTacToeUseCase
import ru.wolfram.client.domain.tic_tac_toe.usecase.HandshakeUseCase
import ru.wolfram.client.domain.tic_tac_toe.usecase.MoveUseCase
import ru.wolfram.client.domain.tic_tac_toe.usecase.RandomTicTacToeUseCase
import ru.wolfram.client.presentation.common.ActionHandler

class TicTacToeViewModel(
    private val randomTicTacToeUseCase: RandomTicTacToeUseCase,
    private val connectUseCase: ConnectUseCase,
    private val handshakeUseCase: HandshakeUseCase,
    private val moveUseCase: MoveUseCase,
    getTicTacToeUseCase: GetTicTacToeUseCase,
    private val ioDispatcher: CoroutineDispatcher
) : ActionHandler<TicTacToeAction>, ViewModel() {
    val ticTacToe = getTicTacToeUseCase()
        .onEach {
            val gk = gameKey.value
            if (gk is WhoResponseState.WhoResponse) {
                _isMove.value = gk.who == it.whoseMove
            }
        }.stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            TicTacToeState()
        )
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

    init {
        viewModelScope.launch {
            gamePath.collectLatest { resp ->
                if (resp is GameCreationResult.GameKey) {
                    handleAction(TicTacToeAction.Connect(resp.key))
                    handleAction(TicTacToeAction.Handshake(resp.name, Who.entries.random()))
                }
            }
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
        viewModelScope.launch(ioDispatcher) {
            _isMove.value = false
            val gk = gameKey.value
            if (gk is WhoResponseState.WhoResponse) {
                moveUseCase(action.x, action.y, gk.key)
            }
        }
    }

}