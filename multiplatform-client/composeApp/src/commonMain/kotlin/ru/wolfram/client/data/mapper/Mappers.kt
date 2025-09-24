package ru.wolfram.client.data.mapper

import ru.wolfram.client.data.network.common.UserCreationResultDto
import ru.wolfram.client.domain.model.common.UserCreationResult

fun UserCreationResultDto.toUserCreationResult() = when (this) {
    is UserCreationResultDto.FailureDto -> UserCreationResult.Failure(msg)
    is UserCreationResultDto.UserKeyDto -> UserCreationResult.UserKey(key)
}