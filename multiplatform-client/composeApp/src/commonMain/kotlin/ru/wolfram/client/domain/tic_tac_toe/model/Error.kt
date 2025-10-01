package ru.wolfram.client.domain.tic_tac_toe.model

sealed interface Error {
    data object Empty : Error

    data object UnexpectedEnd : Error

    data object OpponentNotFound : Error
}