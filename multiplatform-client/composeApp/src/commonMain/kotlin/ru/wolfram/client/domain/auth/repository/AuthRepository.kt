package ru.wolfram.client.domain.auth.repository

import ru.wolfram.client.domain.model.common.UserCreationResult

interface AuthRepository {
    suspend fun auth(name: String): UserCreationResult
}