package ru.wolfram.client.domain.auth.usecase

import ru.wolfram.client.domain.auth.repository.AuthRepository

class AuthUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(name: String) = repository.auth(name)
}