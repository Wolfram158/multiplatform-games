package ru.wolfram.server.model

sealed class UserCreationResult {
    data class UserKey(val name: String, val key: String, val type: String = "UserKey") : UserCreationResult()

    data class Failure(val msg: String, val type: String = "Failure") : UserCreationResult()
}
