package ru.wolfram.client

import io.ktor.client.engine.HttpClientEngine
import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.module.Module

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect val baseHttpUrl: String

expect val wsHost: String

expect val wsPort: Int

expect class HttpClientEngineFactory() {
    fun getHttpClientEngine(): HttpClientEngine
}

expect class DispatcherIO() {
    fun getDispatcher(): CoroutineDispatcher
}

expect class Logger() {
    fun log(tag: String, msg: String)
}

expect val dataStoreModule: Module