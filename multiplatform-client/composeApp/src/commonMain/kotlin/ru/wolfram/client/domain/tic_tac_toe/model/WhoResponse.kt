package ru.wolfram.client.domain.tic_tac_toe.model

sealed interface WhoResponseState {
    object Initial: WhoResponseState

    data class WhoResponse(
        val who: Who,
        val key: String
    ) : WhoResponseState

    data class Failure(val msg: String) : WhoResponseState
}