package ru.wolfram.client.domain.common

sealed interface GameCreationResult {
    object Initial : GameCreationResult
    object Progress : GameCreationResult
    data class GameKey(val name: String, val key: String) : GameCreationResult
    data class Failure(val msg: String) : GameCreationResult
}