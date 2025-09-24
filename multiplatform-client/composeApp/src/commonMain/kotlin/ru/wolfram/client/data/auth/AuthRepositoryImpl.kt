package ru.wolfram.client.data.auth

import ru.wolfram.client.data.mapper.toUserCreationResult
import ru.wolfram.client.data.network.common.ApiService
import ru.wolfram.client.domain.auth.repository.AuthRepository
import ru.wolfram.client.domain.model.common.UserCreationResult

class AuthRepositoryImpl(
    private val apiService: ApiService
): AuthRepository {
    override suspend fun auth(name: String): UserCreationResult {
        return apiService.auth(name).toUserCreationResult()
    }
}