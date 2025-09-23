package ru.wolfram.server.model

sealed class GameCreationResult {
    data class GameKey(val key: String) : GameCreationResult()

    data class Failure(val msg: String) : GameCreationResult()
}
