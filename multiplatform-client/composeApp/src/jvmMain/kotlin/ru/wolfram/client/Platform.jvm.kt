package ru.wolfram.client

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO
import io.ktor.client.engine.okhttp.OkHttp
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.swing.Swing

class JVMPlatform : Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
}

actual fun getPlatform(): Platform = JVMPlatform()

actual val baseHttpUrl: String
    get() = "http://127.0.0.1:8080/api/v1"

actual val wsHost: String
    get() = "127.0.0.1"

actual class HttpClientEngineFactory {
    actual fun getHttpClientEngine(): HttpClientEngine {
        return CIO.create()
    }
}

actual class DispatcherIO actual constructor() {
    actual fun getDispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }
}

actual val wsPort: Int
    get() = 8080

actual class Logger actual constructor() {
    actual fun log(tag: String, msg: String) {
        println("$tag: $msg")
    }
}