package ru.wolfram.client.domain.auth.usecase

import ru.wolfram.client.domain.auth.repository.AuthRepository

class GetAlreadyUsedNameUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke() = repository.getAlreadyUsedName()
}