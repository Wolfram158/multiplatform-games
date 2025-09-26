package ru.wolfram.client.data.mapper

import ru.wolfram.client.data.network.auth.UserCreationResultDto
import ru.wolfram.client.data.network.common.GameCreationResultDto
import ru.wolfram.client.data.network.games.GameDto
import ru.wolfram.client.data.network.games.LabelDto
import ru.wolfram.client.data.network.tic_tac_toe.CellDto
import ru.wolfram.client.data.network.tic_tac_toe.StateDto
import ru.wolfram.client.data.network.tic_tac_toe.TicTacToeStateDto
import ru.wolfram.client.data.network.tic_tac_toe.WhoDto
import ru.wolfram.client.data.network.tic_tac_toe.WhoResponseDto
import ru.wolfram.client.domain.auth.model.UserCreationResult
import ru.wolfram.client.domain.common.GameCreationResult
import ru.wolfram.client.domain.games.model.Game
import ru.wolfram.client.domain.games.model.Label
import ru.wolfram.client.domain.tic_tac_toe.model.Cell
import ru.wolfram.client.domain.tic_tac_toe.model.State
import ru.wolfram.client.domain.tic_tac_toe.model.TicTacToeState
import ru.wolfram.client.domain.tic_tac_toe.model.Who
import ru.wolfram.client.domain.tic_tac_toe.model.WhoResponseState

fun UserCreationResultDto.toUserCreationResult() = when (this) {
    is UserCreationResultDto.FailureDto -> UserCreationResult.Failure(msg)
    is UserCreationResultDto.UserKeyDto -> UserCreationResult.UserKey(name, key)
}

fun LabelDto.toLabel() = when (this) {
    LabelDto.TIC_TAC_TOE -> Label.TIC_TAC_TOE
}

fun GameDto.toGame() = Game(
    name = name,
    label = labelDto.toLabel(),
    randomUrl = randomUrl,
    createUrl = createUrl
)

fun List<GameDto>.toGames() = map { it.toGame() }

fun CellDto.toCell() = when (this) {
    CellDto.CROSS -> Cell.CROSS
    CellDto.NULL -> Cell.NULL
    CellDto.EMPTY -> Cell.EMPTY
}

fun List<List<CellDto>>.toCells() = map { it1 -> it1.map { it2 -> it2.toCell() } }

fun StateDto.toState() = when (this) {
    StateDto.PROGRESS -> State.PROGRESS
    StateDto.WIN_FAILURE -> State.WIN_FAILURE
    StateDto.DRAW -> State.DRAW
}

fun WhoDto.toWho() = when (this) {
    WhoDto.CROSS -> Who.CROSS
    WhoDto.NULL -> Who.NULL
}

fun TicTacToeStateDto.toTicTacToeState() = TicTacToeState(
    state = stateDto.toState(),
    winner = winner,
    loser = loser,
    whoseMove = whoseMove.toWho(),
    cells = cellDtos.toCells()
)

fun GameCreationResultDto.toGameCreationResult() = when (this) {
    is GameCreationResultDto.FailureDto -> GameCreationResult.Failure(msg)
    is GameCreationResultDto.GameKeyDto -> GameCreationResult.GameKey(name, key)
}

fun WhoResponseDto.toWhoResponse() = WhoResponseState.WhoResponse(whoDto.toWho(), key)

fun Who.toWhoDto() = when (this) {
    Who.CROSS -> WhoDto.CROSS
    Who.NULL -> WhoDto.NULL
}