package ru.wolfram.client.domain.auth.usecase

import ru.wolfram.client.domain.auth.repository.AuthRepository

class LoginUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke() = repository.login()
}