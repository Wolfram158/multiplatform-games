package ru.wolfram.client

import io.ktor.client.engine.HttpClientEngine
import kotlinx.coroutines.CoroutineDispatcher

class JsPlatform: Platform {
    override val name: String = "Web with Kotlin/JS"
}

actual fun getPlatform(): Platform = JsPlatform()
actual val baseHttpUrl: String
    get() = TODO("Not yet implemented")
actual val baseWsUrl: String
    get() = TODO("Not yet implemented")

actual class HttpClientEngineFactory {
    actual fun getHttpClientEngine(): HttpClientEngine {
        TODO("Not yet implemented")
    }
}

actual class DispatcherIO actual constructor() {
    actual fun getDispatcher(): CoroutineDispatcher {
        TODO("Not yet implemented")
    }
}