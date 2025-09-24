package ru.wolfram.client.data.network.common

interface ApiService {
    suspend fun auth(name: String): UserCreationResultDto
}