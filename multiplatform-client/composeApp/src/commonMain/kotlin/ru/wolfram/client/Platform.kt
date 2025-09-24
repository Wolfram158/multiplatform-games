package ru.wolfram.client

import io.ktor.client.engine.HttpClientEngine
import kotlinx.coroutines.CoroutineDispatcher

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect val baseHttpUrl: String

expect val baseWsUrl: String

expect class HttpClientEngineFactory() {
    fun getHttpClientEngine(): HttpClientEngine
}

expect class DispatcherIO() {
    fun getDispatcher(): CoroutineDispatcher
}