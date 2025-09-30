package ru.wolfram.client.presentation.tic_tac_toe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.wolfram.client.domain.tic_tac_toe.model.Who
import ru.wolfram.client.domain.tic_tac_toe.usecase.GetTicTacToeUseCase
import ru.wolfram.client.domain.tic_tac_toe.usecase.MoveUseCase
import ru.wolfram.client.presentation.common.ActionHandler

class TicTacToeViewModel(
    private val getTicTacToeUseCase: GetTicTacToeUseCase,
    private val moveUseCase: MoveUseCase
) : ActionHandler<TicTacToeAction>, ViewModel() {
    val ticTacToe = getTicTacToeUseCase(Who.entries.random())
    private val _isMove = MutableStateFlow(false)
    val isMove = _isMove.asStateFlow()

    override fun handleAction(action: TicTacToeAction) {
        when (action) {
            is TicTacToeAction.MakeMove -> reduceMove(action)
        }
    }

    private fun reduceMove(action: TicTacToeAction.MakeMove) {
        viewModelScope.launch {
            moveUseCase(action.x, action.y)
        }
    }

}