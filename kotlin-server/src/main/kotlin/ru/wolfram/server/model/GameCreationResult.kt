package ru.wolfram.server.model

sealed class GameCreationResult {
    data class GameKey(val name: String, val key: String, val type: String = "GameKey") : GameCreationResult()

    data class Failure(val msg: String, val type: String = "Failure") : GameCreationResult()
}
