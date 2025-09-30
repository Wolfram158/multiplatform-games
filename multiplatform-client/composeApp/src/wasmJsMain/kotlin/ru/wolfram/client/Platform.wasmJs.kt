package ru.wolfram.client

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp
import io.ktor.client.engine.HttpClientEngine
import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.module.Module

class WasmPlatform : Platform {
    override val name: String = "Web with Kotlin/Wasm"
}

actual fun getPlatform(): Platform = WasmPlatform()
actual val baseHttpUrl: String
    get() = TODO("Not yet implemented")
actual val wsHost: String
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

actual val wsPort: Int
    get() = TODO("Not yet implemented")

actual class Logger actual constructor() {
    actual fun log(tag: String, msg: String) {
    }
}

actual val dataStoreModule: Module
    get() = TODO("Not yet implemented")