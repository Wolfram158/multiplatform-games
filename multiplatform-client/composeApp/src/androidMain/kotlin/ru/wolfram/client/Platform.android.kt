package ru.wolfram.client

import android.os.Build
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

actual val baseHttpUrl: String
    get() = "http://10.0.2.2:8080/api/v1"

actual val baseWsUrl: String
    get() = "ws://10.0.2.2:8080/tic-tac-toe"

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