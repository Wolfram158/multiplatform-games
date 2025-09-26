package ru.wolfram.client.domain.auth.model

sealed interface UserCreationResult {
    object Initial : UserCreationResult
    object Progress : UserCreationResult
    data class UserKey(val name: String, val key: String) : UserCreationResult
    data class Failure(val msg: String) : UserCreationResult
}