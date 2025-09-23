package ru.wolfram.server.model

sealed class UserCreationResult {
    data class UserKey(val key: String) : UserCreationResult()

    data class Failure(val msg: String) : UserCreationResult()
}
