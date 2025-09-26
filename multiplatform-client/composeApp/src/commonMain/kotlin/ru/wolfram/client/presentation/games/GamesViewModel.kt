package ru.wolfram.client.presentation.games

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.wolfram.client.domain.games.model.GamesResult
import ru.wolfram.client.domain.games.usecase.GetGamesUseCase
import ru.wolfram.client.presentation.common.ActionHandler

class GamesViewModel(
    private val getGamesUseCase: GetGamesUseCase,
    private val ioDispatcher: CoroutineDispatcher
) : ActionHandler<GamesAction>, ViewModel() {
    private val _gamesState = MutableStateFlow<GamesResult>(GamesResult.Initial)
    val gamesState = _gamesState.asStateFlow()

    override fun handleAction(action: GamesAction) {
        when (action) {
            is GamesAction.GetGames -> reduceGetGames(action)
        }
    }

    private fun reduceGetGames(action: GamesAction.GetGames) {
        viewModelScope.launch(ioDispatcher) {
            _gamesState.update {
                GamesResult.Progress
            }
            _gamesState.update {
                getGamesUseCase(action.name, action.key, action.lang)
            }
        }
    }
}