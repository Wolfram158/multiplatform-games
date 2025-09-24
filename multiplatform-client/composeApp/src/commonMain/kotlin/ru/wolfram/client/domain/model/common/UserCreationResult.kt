package ru.wolfram.client.domain.model.common

sealed interface UserCreationResult {
    object Initial : UserCreationResult

    data class UserKey(val key: String) : UserCreationResult

    data class Failure(val msg: String) : UserCreationResult
}