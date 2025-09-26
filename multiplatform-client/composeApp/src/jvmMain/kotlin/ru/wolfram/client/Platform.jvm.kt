package ru.wolfram.client

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class JVMPlatform: Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
}

actual fun getPlatform(): Platform = JVMPlatform()

actual val baseHttpUrl: String
    get() = "http://localhost:8080/api/v1"

actual val wsHost: String
    get() = "localhost"

actual class HttpClientEngineFactory {
    actual fun getHttpClientEngine(): HttpClientEngine {
        return OkHttp.create()
    }
}

actual class DispatcherIO actual constructor() {
    actual fun getDispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }
}

actual val wsPort: Int
    get() = 8080